package pl.edu.agh.cs.kraksim.simulation.web.request

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.controller.requestBody.CreateLightPhaseStrategyRequest
import pl.edu.agh.cs.kraksim.controller.requestBody.CreateMovementSimulationStrategyRequest
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationType

class CreateSimulationRequest {
    lateinit var name: String
    var mapId: Long = 0
    lateinit var simulationType: SimulationType
    lateinit var expectedVelocity: Map<RoadId, Velocity>
    lateinit var movementSimulationStrategy: CreateMovementSimulationStrategyRequest
    lateinit var lightPhaseStrategies: List<CreateLightPhaseStrategyRequest>
}
