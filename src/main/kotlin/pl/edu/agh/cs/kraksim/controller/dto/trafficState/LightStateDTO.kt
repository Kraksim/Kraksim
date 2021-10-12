package pl.edu.agh.cs.kraksim.controller.dto.trafficState

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase
import pl.edu.agh.cs.kraksim.trafficState.domain.AlgorithmType

class TrafficLightDTO(
    var intersectionId: IntersectionId,
    var phases: List<PhaseDTO>
) {
    var id: Long = 0
}

class PhaseDTO(
    var laneId: LaneId,
    var phaseTime: Int,
    var state: TrafficLightPhase.LightColor
) {
    var id: Long = 0
}

class LightPhaseStrategyDTO(
    var algorithm: AlgorithmType,
    var turnLength: Int,
    var intersections: List<IntersectionId>
) {
    var id: Long = 0
}
