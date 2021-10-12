package pl.edu.agh.cs.kraksim.controller.dto.trafficState

import pl.edu.agh.cs.kraksim.trafficState.domain.StateType

class SimulationStateDTO(
    var turn: Long = 0,
    var carsOnMap: List<CarDTO>,
    var trafficLights: List<TrafficLightDTO>,
    var gatewaysStates: List<GatewayStateDTO>,
    var stateType: StateType
) {
    var id: Long = 0
}
