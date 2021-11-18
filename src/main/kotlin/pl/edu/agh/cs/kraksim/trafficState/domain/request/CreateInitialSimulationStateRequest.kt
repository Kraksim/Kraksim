package pl.edu.agh.cs.kraksim.trafficState.domain.request

import javax.validation.constraints.NotEmpty

class CreateInitialSimulationStateRequest(
    @field:NotEmpty
    var gatewaysStates: List<CreateGatewayStateRequest>
)
