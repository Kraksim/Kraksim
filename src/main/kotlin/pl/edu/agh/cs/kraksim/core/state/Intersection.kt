package pl.edu.agh.cs.kraksim.core.state

import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelRoad
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase

interface Intersection : RoadNode {
    val phases: Map<LaneId, TrafficLightPhase>
    val directions: List<IntersectionTurningLaneDirection>
    fun lightPhasesOf(road: Road): List<TrafficLightPhase>
    fun getPossibleRoads(lane: Lane): List<Road>
    fun getPossibleRoads(road: Road): List<NagelRoad>
    fun getLanesLeadingTo(road: RoadId): Set<LaneId>
}
