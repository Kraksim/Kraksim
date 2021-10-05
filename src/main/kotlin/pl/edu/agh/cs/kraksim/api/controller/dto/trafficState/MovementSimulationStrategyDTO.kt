package pl.edu.agh.cs.kraksim.api.controller.dto.trafficState

import pl.edu.agh.cs.kraksim.repository.entities.trafficState.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.RandomProviderType

class MovementSimulationStrategyDTO(
    var type: MovementSimulationStrategyType,
    var randomProvider: RandomProviderType,
    var slowDownProbability: Double,
    var maxVelocity: Int
) {
    var id: Long = 0
}
