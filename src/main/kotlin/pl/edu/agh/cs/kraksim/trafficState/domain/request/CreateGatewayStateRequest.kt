package pl.edu.agh.cs.kraksim.trafficState.domain.request

import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.generator.request.CreateGeneratorRequest
import javax.validation.Valid

class CreateGatewayStateRequest {
    var gatewayId: GatewayId = 0

    @field:Valid
    lateinit var generators: List<CreateGeneratorRequest>
}
