package pl.edu.agh.cs.kraksim.trafficState.domain.dto

import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.RandomProviderType

class MovementSimulationStrategyDTO(
    var type: MovementSimulationStrategyType,
    var randomProvider: RandomProviderType,
    var slowDownProbability: Double,
    var maxVelocity: Int,
    var threshold: Int? = null,
    var accelerationDelayProbability: Double? = null,
    var breakLightReactionProbability: Double? = null
) {
    var id: Long = 0
}
