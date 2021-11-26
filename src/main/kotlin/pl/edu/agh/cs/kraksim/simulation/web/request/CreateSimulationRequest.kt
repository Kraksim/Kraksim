package pl.edu.agh.cs.kraksim.simulation.web.request

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationType
import pl.edu.agh.cs.kraksim.trafficLight.web.request.CreateLightPhaseStrategyRequest
import pl.edu.agh.cs.kraksim.trafficState.domain.request.CreateInitialSimulationStateRequest
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive

class CreateSimulationRequest(
    @field:NotBlank
    var name: String,
    var mapId: Long = 0,
    var simulationType: SimulationType,
    @field:Valid
    var expectedVelocity: Map<RoadId, @Positive Velocity>,
    @field:Valid
    var movementSimulationStrategy: CreateMovementSimulationStrategyRequest,
    @field:Valid
    @field:NotEmpty
    var lightPhaseStrategies: List<CreateLightPhaseStrategyRequest>,
    @field:Valid
    var initialState: CreateInitialSimulationStateRequest
)
