package pl.edu.agh.cs.kraksim.controller.requestBody

import pl.edu.agh.cs.kraksim.trafficState.domain.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.trafficState.domain.RandomProviderType

class CreateMovementSimulationStrategyRequest {
    lateinit var type: MovementSimulationStrategyType
    lateinit var randomProvider: RandomProviderType
    var slowDownProbability: Double = 0.0
    var maxVelocity: Int = 0
}
