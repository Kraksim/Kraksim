package pl.edu.agh.cs.kraksim.simulation.web.request

import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.RandomProviderType

class CreateMovementSimulationStrategyRequest {
    lateinit var type: MovementSimulationStrategyType
    lateinit var randomProvider: RandomProviderType
    var slowDownProbability: Double = 0.0
    var maxVelocity: Int = 0
}
