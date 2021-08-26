package pl.edu.agh.cs.kraksim.controller.dto.trafficState

import pl.edu.agh.cs.kraksim.controller.dto.SimulationDTO
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.StateType

class SimulationStateDTO(
    var turn: Long = 0,
    var carsOnMap: List<CarDTO>,
    var trafficLights: List<TrafficLightDTO>,
    var gatewaysStates: List<GatewayStateDTO>,
    var stateType: StateType
) {

    lateinit var simulation: SimulationDTO
    var id: Long = 0
}
