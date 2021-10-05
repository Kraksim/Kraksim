package pl.edu.agh.cs.kraksim.model.factory

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.SimulationState
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.AlgorithmType
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.LightPhaseStrategyEntity
import pl.edu.agh.cs.kraksim.model.trafficLight.LightPhaseManager
import pl.edu.agh.cs.kraksim.model.trafficLight.LightPhaseStrategy
import pl.edu.agh.cs.kraksim.model.trafficLight.strategies.TurnBasedLightPhaseStrategy

@Component
class LightPhaseManagerFactory {

    fun from(state: SimulationState, lightPhaseStrategies: List<LightPhaseStrategyEntity>): LightPhaseManager {
        return LightPhaseManager(
            simulationState = state,
            strategies = createStrategies(lightPhaseStrategies)
        )
    }

    private fun createStrategies(lightPhaseStrategies: List<LightPhaseStrategyEntity>): Map<LightPhaseStrategy, List<IntersectionId>> {
        return lightPhaseStrategies.associate { createLightPhaseStrategy(it) to it.intersections }
    }

    private fun createLightPhaseStrategy(it: LightPhaseStrategyEntity) = when (it.algorithm) {
        AlgorithmType.TURN_BASED -> createTurnBasedStrategy(it)
    }

    private fun createTurnBasedStrategy(it: LightPhaseStrategyEntity) = TurnBasedLightPhaseStrategy(it.turnLength, it.id)
}
