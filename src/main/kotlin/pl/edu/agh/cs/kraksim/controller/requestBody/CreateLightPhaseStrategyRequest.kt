package pl.edu.agh.cs.kraksim.controller.requestBody

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.AlgorithmType

class CreateLightPhaseStrategyRequest {
    lateinit var algorithm: AlgorithmType
    var turnLength: Int = 0
    lateinit var intersections: List<IntersectionId>
}
