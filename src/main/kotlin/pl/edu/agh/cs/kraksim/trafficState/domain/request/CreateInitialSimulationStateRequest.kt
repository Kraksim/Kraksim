package pl.edu.agh.cs.kraksim.trafficState.domain.request

class CreateInitialSimulationStateRequest {
    lateinit var trafficLights: List<CreateTrafficLightRequest>
    lateinit var gatewaysStates: List<CreateGatewayStateRequest>
}
