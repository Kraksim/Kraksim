package pl.edu.agh.cs.kraksim.trafficState.domain.request

import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase

class CreatePhaseRequest {
    var laneId: LaneId = 0
    lateinit var state: TrafficLightPhase.LightColor
}
