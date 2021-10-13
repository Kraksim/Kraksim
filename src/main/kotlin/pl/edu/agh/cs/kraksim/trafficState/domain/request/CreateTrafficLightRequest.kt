package pl.edu.agh.cs.kraksim.trafficState.domain.request

import pl.edu.agh.cs.kraksim.common.IntersectionId

class CreateTrafficLightRequest {
    var intersectionId: IntersectionId = 0
    lateinit var phases: List<CreatePhaseRequest>
}
