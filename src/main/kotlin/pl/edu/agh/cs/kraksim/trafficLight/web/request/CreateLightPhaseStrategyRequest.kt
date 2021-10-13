package pl.edu.agh.cs.kraksim.trafficLight.web.request

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.AlgorithmType

class CreateLightPhaseStrategyRequest {
    lateinit var algorithm: AlgorithmType
    var turnLength: Int = 0
    lateinit var intersections: List<IntersectionId>
}
