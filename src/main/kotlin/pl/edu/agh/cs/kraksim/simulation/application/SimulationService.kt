package pl.edu.agh.cs.kraksim.simulation.application

import org.apache.logging.log4j.LogManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.exception.InvalidSimulationStateConfigurationException
import pl.edu.agh.cs.kraksim.common.exception.ObjectNotFoundException
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.gps.GpsFactory
import pl.edu.agh.cs.kraksim.simulation.db.BasicSimulationInfo
import pl.edu.agh.cs.kraksim.simulation.db.SimulationRepository
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationEntity
import pl.edu.agh.cs.kraksim.simulation.web.request.CreateSimulationRequest
import pl.edu.agh.cs.kraksim.statistics.application.StatisticsFactory
import pl.edu.agh.cs.kraksim.trafficLight.application.LightPhaseManagerFactory
import pl.edu.agh.cs.kraksim.trafficState.application.MovementSimulationStrategyFactory
import pl.edu.agh.cs.kraksim.trafficState.application.StateFactory

@Service
class SimulationService(
    val repository: SimulationRepository,
    val stateFactory: StateFactory,
    val movementSimulationStrategyFactory: MovementSimulationStrategyFactory,
    val lightPhaseManagerFactory: LightPhaseManagerFactory,
    val statisticsFactory: StatisticsFactory,
    val simulationFactory: SimulationFactory,
    val gpsFactory: GpsFactory,
    val requestMapper: RequestToEntityMapper,
    val mapService: MapService
) {
    private val log = LogManager.getLogger()

    @Transactional
    fun simulateStep(simulationId: Long, times: Int = 1) {
        val simulationEntity = repository.getByIdWithLock(simulationId)
            ?: throw ObjectNotFoundException("Couldn't find simulation with id = $simulationId")
        log.info("Simulation id=$simulationId runs simulate for $times times, current turn=${simulationEntity.latestTrafficStateEntity.turn}")

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
        if (simulationState.turn == 0L) {
            lightPhaseManager.initializeLights()
        }
        repeat(times) {
            if (simulationState.finished) {
                return@repeat
            }
            simulation.step()

            checkIfFinished(simulation.state)
            val stateEntity = stateFactory.toEntity(simulation.state, simulationEntity)

            simulationEntity.apply {
                simulationStateEntities.add(stateEntity)
                statisticsEntities +=
                    statisticsFactory.createStatisticsEntity(statisticsManager.latestState, simulationEntity)
                finished = simulation.state.finished
            }
        }
        log.info("Simulation id=$simulationId finished simulating, current turn=${simulationEntity.latestTrafficStateEntity.turn}")
        repository.save(simulationEntity)
        log.info("Simulation id=$simulationId has been saved")
    }

    fun getSimulation(id: Long): SimulationEntity {
        log.info("Fetching simulation id=$id")
        return repository.findByIdOrNull(id) ?: throw ObjectNotFoundException("Couldn't find simulation with id = $id")
    }

    fun createSimulation(request: CreateSimulationRequest): SimulationEntity {
        log.info("Creating simulation name=${request.name}, mapId=${request.mapId}")
        val mapEntity = mapService.getById(request.mapId)
        val simulationEntity = requestMapper.createSimulation(request, mapEntity)
        val simulationState = stateFactory.from(simulationEntity)

        val exceptions: List<String> = validateState(simulationState, simulationEntity)
        if (exceptions.isNotEmpty()) {
            throw InvalidSimulationStateConfigurationException(exceptions)
        }

        return repository.save(simulationEntity)
    }

    fun deleteSimulation(id: Long) {
        log.info("Deleting simulation id=$id")
        return repository.deleteById(id)
    }

    fun getAllSimulationsInfo(): List<BasicSimulationInfo> {
        log.info("Fetching all simulation basic info")
        return repository.findAllBy()
    }

    private fun checkIfFinished(simulationState: SimulationState) {
        simulationState.finished = simulationState.cars.isEmpty() &&
            simulationState.gateways
                .values
                .asSequence()
                .flatMap { it.generators }
                .all { it.carsToRelease == 0 }
    }

    private fun validateState(simulationState: SimulationState, simulationEntity: SimulationEntity): List<String> {
        return listOf(
            validateLightPhaseStrategies(simulationState, simulationEntity),
            validateGenerators(simulationState),
            validateBrakeLight(simulationEntity)
        ).flatten()
    }

    private fun validateBrakeLight(simulationEntity: SimulationEntity): List<String> {

        val movementSimulationStrategy = simulationEntity.movementSimulationStrategy
        return if (
            movementSimulationStrategy.type == MovementSimulationStrategyType.BRAKE_LIGHT &&
            (
                movementSimulationStrategy.threshold == null ||
                    movementSimulationStrategy.accelerationDelayProbability == null ||
                    movementSimulationStrategy.breakLightReactionProbability == null
                )
        ) listOf("Lacking parameters for brake light strategy")
        else emptyList()
    }

    private fun validateLightPhaseStrategies(
        simulationState: SimulationState,
        simulationEntity: SimulationEntity
    ): List<String> {
        val intersectionsWithStrategy: Set<IntersectionId> =
            simulationEntity.lightPhaseStrategies.flatMap { strategy -> strategy.intersections }.toSet()
        return simulationState.intersections.keys.mapNotNull {
            if (!intersectionsWithStrategy.contains(it)) {
                it
            } else {
                null
            }
        }.map { "Intersection with id=$it doesn't contain traffic light strategy" }
    }

    private fun validateGenerators(simulationState: SimulationState) =
        simulationState.gateways.flatMap { (_, gateway) -> gateway.generators.map { Pair(gateway, it) } }
            .mapNotNull { (gateway, generator) ->
                try {
                    gpsFactory.from(gateway, generator, simulationState)
                } catch (e: Exception) {
                    return@mapNotNull when (e) {
                        is IllegalArgumentException, is IllegalStateException -> e.message
                        else -> throw e
                    }
                }
                return@mapNotNull null
            }
}
