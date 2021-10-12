package pl.edu.agh.cs.kraksim.controller.requestBody

import pl.edu.agh.cs.kraksim.repository.entities.trafficState.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.RandomProviderType

class CreateMovementSimulationStrategyRequest {
    lateinit var type: MovementSimulationStrategyType
    lateinit var randomProvider: RandomProviderType
    var slowDownProbability: Double = 0.0
    var maxVelocity: Int = 0
}
