package pl.edu.agh.cs.kraksim.simulation.web.request

import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.RandomProviderType
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Positive

class CreateMovementSimulationStrategyRequest {
    lateinit var type: MovementSimulationStrategyType
    lateinit var randomProvider: RandomProviderType
    @field:Min(0)
    @field:Max(1)
    var slowDownProbability: Double = 0.0
    @field:Positive
    var maxVelocity: Int = 0
    var threshold: Int? = null
    var accelerationDelayProbability: Double? = null
    var breakLightReactionProbability: Double? = null
}
