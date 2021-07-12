package pl.edu.agh.cs.kraksim.api.factory

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.repository.entities.AlgorithmType
import pl.edu.agh.cs.kraksim.repository.entities.LightPhaseStrategyEntity
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseManager
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseStrategy
import pl.edu.agh.cs.kraksim.trafficLight.strategies.TurnBasedLightPhaseStrategy

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

    private fun createTurnBasedStrategy(it: LightPhaseStrategyEntity) = TurnBasedLightPhaseStrategy(it.turnLength)
}
