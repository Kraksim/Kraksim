package pl.edu.agh.cs.kraksim.controller.requestBody

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.AlgorithmType

class CreateLightPhaseStrategyRequest {
    public lateinit var algorithm: AlgorithmType
    public var turnLength: Int = 0
    public lateinit var intersections: List<IntersectionId>
}
