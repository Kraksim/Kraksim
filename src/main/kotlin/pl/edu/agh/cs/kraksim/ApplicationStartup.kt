package pl.edu.agh.cs.kraksim

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.api.Service
import pl.edu.agh.cs.kraksim.gps.algorithms.RoadLengthGPS
import pl.edu.agh.cs.kraksim.repository.MapRepository
import pl.edu.agh.cs.kraksim.repository.RoadRepository
import pl.edu.agh.cs.kraksim.repository.SimulationRepository

@Component
class ApplicationStartup(
    val roadLengthGPS: RoadLengthGPS,
    val simprep: SimulationRepository,
    val service: Service,
    val mapRepository: MapRepository,
    val roadRepository: RoadRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        service.simulateStep(6, 2)

//        var lane = LaneEntity(
//            startingPoint = 0,
//            endingPoint = 400,
//            indexFromLeft = 0
//        )
//        var road = RoadEntity(
//            length = 400,
//            lanes = listOf(lane)
//        )
//
//        var mapEntity = MapEntity(
//            type = MapType.MAP,
//            roadNodes = listOf(
//                RoadNodeEntity(
//                    type = RoadNodeType.GATEWAY,
//                    position = PositionEntity(1.0, 1.0),
//                    endingRoads = emptyList(),
//                    startingRoads = listOf(road),
//                    turnDirections = emptyList()
//                ),
//                RoadNodeEntity(
//                    type = RoadNodeType.GATEWAY,
//                    position = PositionEntity(21.0, 1.0),
//                    endingRoads = listOf(road),
//                    startingRoads = emptyList(),
//                    turnDirections = emptyList()
//                )
//            ),
//            roads = listOf(road)
//        )
//        mapRepository.save(mapEntity)
//
//        mapEntity = mapRepository.getById(1)
//        lane = road.lanes.first()
//
//        var simulationEntity = SimulationEntity(
//            mapEntity = mapEntity,
//            simulationStateEntities = ArrayList(),
//            movementSimulationStrategy = MovementSimulationStrategyEntity(
//                type = MovementSimulationStrategyType.NAGEL_SCHRECKENBERG,
//                randomProvider = RandomProviderType.TRUE,
//                slowDownProbability = 0.3,
//                maxVelocity = 6
//            ),
//            simulationType = SimulationType.NAGEL_CORE,
//            expectedVelocity = emptyMap(),
//            lightPhaseStrategies = emptyList()
//        )
//
//        var simulationStateEntity = SimulationStateEntity(
//            turn = 0,
//            trafficLights = emptyList(),
//            simulation = simulationEntity,
//            stateType = StateType.NAGEL_SCHRECKENBERG,
//            gatewaysStates = emptyList(),
//            carsOnMap = listOf(
//                CarEntity(
//                    carId = 1,
//                    velocity = 2,
//                    currentLaneId = lane.id,
//                    positionRelativeToStart = 6,
//                    gps = GPSEntity(
//                        type = GPSType.DIJKSTRA_ROAD_LENGTH,
//                        route = emptyList()
//                    )
//                ),
//                CarEntity(
//                    carId = 2,
//                    velocity = 6,
//                    currentLaneId = lane.id,
//                    positionRelativeToStart = 10,
//                    gps = GPSEntity(
//                        type = GPSType.DIJKSTRA_ROAD_LENGTH,
//                        route = emptyList()
//                    )
//                )
//            )
//        )
//        simulationEntity.simulationStateEntities.add(simulationStateEntity)
//        simulationEntity = simprep.save(simulationEntity)
//
//        service.simulateStep(simulationEntity.id, 1)
    }
}
