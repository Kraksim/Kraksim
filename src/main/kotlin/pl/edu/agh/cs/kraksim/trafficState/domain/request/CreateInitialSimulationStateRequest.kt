package pl.edu.agh.cs.kraksim.trafficState.domain.request

class CreateInitialSimulationStateRequest {
    lateinit var gatewaysStates: List<CreateGatewayStateRequest>
}
