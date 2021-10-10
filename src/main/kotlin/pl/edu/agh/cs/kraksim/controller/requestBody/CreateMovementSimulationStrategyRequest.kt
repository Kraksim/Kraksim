package pl.edu.agh.cs.kraksim.controller.requestBody

import pl.edu.agh.cs.kraksim.repository.entities.trafficState.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.RandomProviderType

class CreateMovementSimulationStrategyRequest {
    public lateinit var type: MovementSimulationStrategyType
    public lateinit var randomProvider: RandomProviderType
    public var slowDownProbability: Double = 0.0
    public var maxVelocity: Int = 0
}
