package pl.edu.agh.cs.kraksim.trafficState.domain.dto

import pl.edu.agh.cs.kraksim.trafficState.domain.entity.StateType

class SimulationStateDTO(
    var turn: Long = 0,
    var carsOnMap: List<CarDTO>,
    var trafficLights: List<TrafficLightDTO>,
    var gatewaysStates: List<GatewayStateDTO>,
    var stateType: StateType
) {
    var id: Long = 0
}
