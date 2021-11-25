package pl.edu.agh.cs.kraksim.trafficLight.web.request

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.AlgorithmType
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Positive

class CreateLightPhaseStrategyRequest {
    lateinit var algorithm: AlgorithmType
    @field:Positive
    var turnLength: Int? = null
    @field:Positive @field:Max(1)
    var phiFactor: Double? = null
    @field:Positive
    var minPhaseLength: Int? = null
    lateinit var intersections: List<IntersectionId>
}
