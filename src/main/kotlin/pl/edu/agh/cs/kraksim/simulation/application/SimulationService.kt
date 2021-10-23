package pl.edu.agh.cs.kraksim.simulation.application

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.common.exception.ObjectNotFoundException
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.simulation.db.BasicSimulationInfo
import pl.edu.agh.cs.kraksim.simulation.db.MapRepository
import pl.edu.agh.cs.kraksim.simulation.db.SimulationRepository
import pl.edu.agh.cs.kraksim.simulation.domain.*
import pl.edu.agh.cs.kraksim.simulation.web.request.CreateSimulationRequest
import pl.edu.agh.cs.kraksim.statistics.application.StatisticsFactory
import pl.edu.agh.cs.kraksim.trafficLight.application.LightPhaseManagerFactory
import pl.edu.agh.cs.kraksim.trafficState.application.MovementSimulationStrategyFactory
import pl.edu.agh.cs.kraksim.trafficState.application.StateFactory
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.*

@Service
class SimulationService(
    val repository: SimulationRepository,
    val stateFactory: StateFactory,
    val movementSimulationStrategyFactory: MovementSimulationStrategyFactory,
    val lightPhaseManagerFactory: LightPhaseManagerFactory,
    val statisticsFactory: StatisticsFactory,
    val simulationFactory: SimulationFactory,
    val mapRepository: MapRepository,
    val requestMapper: RequestToEntityMapper
) {

    fun simulateStep(simulationId: Long = 0L, times: Int = 1): SimulationEntity {

        var simulationEntity = repository.getById(simulationId)
        val simulationState = stateFactory.from(simulationEntity)
        val movementStrategy =
            movementSimulationStrategyFactory.from(simulationEntity.movementSimulationStrategy)
        val lightPhaseManager =
            lightPhaseManagerFactory.from(simulationState, simulationEntity.lightPhaseStrategies)
        val statisticsManager = statisticsFactory.createStatisticsManager(
            simulationEntity.statisticsEntities,
            simulationEntity.expectedVelocity
        )

        val simulation = simulationFactory.from(
            simulationType = simulationEntity.simulationType,
            simulationState = simulationState,
            movementStrategy = movementStrategy,
            lightPhaseManager = lightPhaseManager,
            statisticsManager = statisticsManager
        )

        repeat(times) {
            simulation.step()
            val stateEntity = stateFactory.toEntity(simulation.state, simulationEntity)
            simulationEntity.simulationStateEntities.add(stateEntity)
            simulationEntity.statisticsEntities += (
                statisticsFactory.createStatisticsEntity(
                    statisticsManager.latestState,
                    simulationEntity
                )
                )
            simulationEntity = repository.save(simulationEntity)
        }

        return simulationEntity
    }

    fun getSimulation(id: Long): SimulationEntity? {
        return repository.findByIdOrNull(id)
    }

    fun getAllSimulations(): List<SimulationEntity> {
        return repository.findAll()
    }

    fun createSimulation(request: CreateSimulationRequest): SimulationEntity {
        val mapEntity = mapRepository.findById(request.mapId)
        if (mapEntity.isEmpty) throw ObjectNotFoundException("Couldn't find map with id " + request.mapId)
        val simulation = requestMapper.createSimulation(request, mapEntity.get())

        return repository.save(simulation)
    }

    fun populate(): SimulationEntity {
        var lane = LaneEntity(
            startingPoint = 0,
            endingPoint = 400,
            indexFromLeft = 0
        )
        val road = RoadEntity(
            length = 400,
            lanes = arrayListOf(lane)
        )

        var mapEntity = MapEntity(
            type = MapType.MAP,
            roadNodes = arrayListOf(
                RoadNodeEntity(
                    type = RoadNodeType.GATEWAY,
                    position = PositionEntity(1.0, 1.0),
                    endingRoads = ArrayList(),
                    startingRoads = arrayListOf(road),
                    turnDirections = ArrayList()
                ),
                RoadNodeEntity(
                    type = RoadNodeType.GATEWAY,
                    position = PositionEntity(21.0, 1.0),
                    endingRoads = arrayListOf(road),
                    startingRoads = ArrayList(),
                    turnDirections = ArrayList()
                )
            ),
            roads = arrayListOf(road)
        )

        mapEntity = mapRepository.save(mapEntity)
        lane = road.lanes.first()

        val simulationEntity = SimulationEntity(
            name = "POPULATED_SIMULATION",
            mapEntity = mapEntity,
            simulationStateEntities = ArrayList(),
            movementSimulationStrategy = MovementSimulationStrategyEntity(
                type = MovementSimulationStrategyType.NAGEL_SCHRECKENBERG,
                randomProvider = RandomProviderType.TRUE,
                slowDownProbability = 0.3,
                maxVelocity = 6
            ),
            simulationType = SimulationType.NAGEL_CORE,
            expectedVelocity = emptyMap(),
            lightPhaseStrategies = ArrayList(),
            statisticsEntities = ArrayList()
        )

        val simulationStateEntity = SimulationStateEntity(
            turn = 0,
            trafficLights = ArrayList(),
            stateType = StateType.NAGEL_SCHRECKENBERG,
            gatewaysStates = ArrayList(),
            carsOnMap = arrayListOf(
                CarEntity(
                    carId = 1,
                    velocity = 2,
                    currentLaneId = lane.id,
                    positionRelativeToStart = 30,
                    gps = GPSEntity(
                        type = GPSType.DIJKSTRA_ROAD_LENGTH,
                        route = ArrayList()
                    )
                ),
                CarEntity(
                    carId = 2,
                    velocity = 6,
                    currentLaneId = lane.id,
                    positionRelativeToStart = 50,
                    gps = GPSEntity(
                        type = GPSType.DIJKSTRA_ROAD_LENGTH,
                        route = ArrayList()
                    )
                )
            )
        )
        simulationEntity.simulationStateEntities.add(simulationStateEntity)
        return repository.save(simulationEntity)
    }

    fun deleteSimulation(id: Long) {
        return repository.deleteById(id)
    }

    fun getAllSimulationsInfo(): List<BasicSimulationInfo> {
        return repository.findAllBy()
    }
}
