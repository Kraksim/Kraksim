package pl.edu.agh.cs.kraksim.api

import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.api.factory.LightPhaseManagerFactory
import pl.edu.agh.cs.kraksim.api.factory.MovementSimulationStrategyFactory
import pl.edu.agh.cs.kraksim.api.factory.SimulationFactory
import pl.edu.agh.cs.kraksim.api.factory.StateFactory
import pl.edu.agh.cs.kraksim.repository.MapRepository

@Service
class Service(
    val repository: MapRepository,
    val stateFactory: StateFactory,
    val movementSimulationStrategyFactory: MovementSimulationStrategyFactory,
    val lightPhaseManagerFactory: LightPhaseManagerFactory,
    val statisticsService: StatisticsService,
    val simulationFactory: SimulationFactory
) {

    fun simulateStep(simulationId: Long = 0L, times: Int = 1) {

        val simulationEntity = repository.getById(simulationId)
        val simulationState = stateFactory.from(simulationEntity)
        val movementStrategy =
            movementSimulationStrategyFactory.from(simulationEntity.trafficStateEntity.movementSimulationStrategy)
        val lightPhaseManager =
            lightPhaseManagerFactory.from(simulationState, simulationEntity.trafficStateEntity.lightPhaseStrategies)
        val statisticsManager = statisticsService.createStatisticsManager(
            simulationId,
            simulationEntity.trafficStateEntity.expectedVelocity
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
        }

        //TODO zapisać to gówno do bazy spowrotem
    }
}
