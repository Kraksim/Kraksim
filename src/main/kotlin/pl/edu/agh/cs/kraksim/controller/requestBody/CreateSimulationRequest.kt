package pl.edu.agh.cs.kraksim.controller.requestBody

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.repository.entities.SimulationType

class CreateSimulationRequest {
    public lateinit var name: String
    public var mapId: Long = 0
    public lateinit var simulationType: SimulationType
    public lateinit var expectedVelocity: Map<RoadId, Velocity>
    public lateinit var movementSimulationStrategy: CreateMovementSimulationStrategyRequest
    public lateinit var lightPhaseStrategies: List<CreateLightPhaseStrategyRequest>
}
