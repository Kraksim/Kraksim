package pl.edu.agh.cs.kraksim.api

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.api.factory.*
import pl.edu.agh.cs.kraksim.repository.SimulationRepository
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity

@Service
class SimulationService(
    val repository: SimulationRepository,
    val stateFactory: StateFactory,
    val movementSimulationStrategyFactory: MovementSimulationStrategyFactory,
    val lightPhaseManagerFactory: LightPhaseManagerFactory,
    val statisticsFactory: StatisticsFactory,
    val simulationFactory: SimulationFactory
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
}
