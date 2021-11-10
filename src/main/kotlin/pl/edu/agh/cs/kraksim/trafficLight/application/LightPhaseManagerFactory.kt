package pl.edu.agh.cs.kraksim.trafficLight.application

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.trafficLight.application.strategy.SOTLLightPhaseStrategy
import pl.edu.agh.cs.kraksim.trafficLight.application.strategy.TurnBasedLightPhaseStrategy
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.AlgorithmType
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.LightPhaseStrategyEntity

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
        AlgorithmType.SOTL -> createSOTLStrategy(it)
    }

    private fun createTurnBasedStrategy(it: LightPhaseStrategyEntity) = TurnBasedLightPhaseStrategy(it.turnLength!!, it.id)

    private fun createSOTLStrategy(it: LightPhaseStrategyEntity) = SOTLLightPhaseStrategy(
        phiFactor = it.phiFactor!!,
        minPhaseLength = it.minPhaseLength!!,
        omegaMin = it.omegaMin!!,
        ni = it.ni!!,
        id = it.id
    )
}
