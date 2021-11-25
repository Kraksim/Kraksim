package pl.edu.agh.cs.kraksim.simulation.web.request

import org.springframework.validation.annotation.Validated
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationType
import pl.edu.agh.cs.kraksim.trafficLight.web.request.CreateLightPhaseStrategyRequest
import pl.edu.agh.cs.kraksim.trafficState.domain.request.CreateInitialSimulationStateRequest
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
@Validated
class CreateSimulationRequest(
        @field:NotBlank
        var name: String,
        var mapId: Long = 0,
        var simulationType: SimulationType,
        var expectedVelocity: Map<RoadId, @Positive Velocity>,
        var movementSimulationStrategy: CreateMovementSimulationStrategyRequest,
        var lightPhaseStrategies: List<CreateLightPhaseStrategyRequest>,
        var initialState: CreateInitialSimulationStateRequest
)
