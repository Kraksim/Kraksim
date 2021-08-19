package pl.edu.agh.cs.kraksim.api

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.cs.kraksim.api.factory.LightPhaseManagerFactory
import pl.edu.agh.cs.kraksim.api.factory.MovementSimulationStrategyFactory
import pl.edu.agh.cs.kraksim.api.factory.SimulationFactory
import pl.edu.agh.cs.kraksim.api.factory.StateFactory
import pl.edu.agh.cs.kraksim.repository.SimulationRepository

@Service
class SimulationService(
    val repository: SimulationRepository,
    val stateFactory: StateFactory,
    val movementSimulationStrategyFactory: MovementSimulationStrategyFactory,
    val lightPhaseManagerFactory: LightPhaseManagerFactory,
    val statisticsService: StatisticsService,
    val simulationFactory: SimulationFactory
) {

    @Transactional // todo remove after figuring out eager fetching whole entity
    fun simulateStep(simulationId: Long = 0L, times: Int = 1) {

        var simulationEntity = repository.getById(simulationId)
        val simulationState = stateFactory.from(simulationEntity)
        val movementStrategy =
            movementSimulationStrategyFactory.from(simulationEntity.movementSimulationStrategy)
        val lightPhaseManager =
            lightPhaseManagerFactory.from(simulationState, simulationEntity.lightPhaseStrategies)
        val statisticsManager = statisticsService.createStatisticsManager(
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
            simulationEntity.statisticsEntities += (statisticsService.createStatisticsEntity(
                statisticsManager.latestState,
                simulationEntity
            ))
            simulationEntity = repository.save(simulationEntity)
        }
    }
}
