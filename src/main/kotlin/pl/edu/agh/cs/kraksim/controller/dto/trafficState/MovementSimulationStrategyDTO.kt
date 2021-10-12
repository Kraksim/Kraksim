package pl.edu.agh.cs.kraksim.controller.dto.trafficState

import pl.edu.agh.cs.kraksim.trafficState.domain.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.trafficState.domain.RandomProviderType

class MovementSimulationStrategyDTO(
    var type: MovementSimulationStrategyType,
    var randomProvider: RandomProviderType,
    var slowDownProbability: Double,
    var maxVelocity: Int
) {
    var id: Long = 0
}
