package pl.edu.agh.cs.kraksim.trafficLight.web.request

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.AlgorithmType

class CreateLightPhaseStrategyRequest {
    lateinit var algorithm: AlgorithmType
    var turnLength: Int? = null
    var phiFactor: Double? = null
    var minPhaseLength: Int? = null
    var omegaMin: Int? = null
    var ni: Int? = null
    lateinit var intersections: List<IntersectionId>
}
