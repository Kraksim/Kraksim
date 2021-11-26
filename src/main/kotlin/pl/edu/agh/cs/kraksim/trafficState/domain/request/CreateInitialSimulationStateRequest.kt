package pl.edu.agh.cs.kraksim.trafficState.domain.request

import javax.validation.Valid
import javax.validation.constraints.NotEmpty

class CreateInitialSimulationStateRequest(
    @field:Valid
    @field:NotEmpty
    var gatewaysStates: List<@Valid CreateGatewayStateRequest>
)
