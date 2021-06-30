package pl.edu.agh.cs.kraksim.nagelCore.state

import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.core.state.Lane
import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase

class NagelIntersection(
    override val id: Long,
    val directions: Set<NagelIntersectionTurningLaneDirection>,
    override val endingRoads: List<NagelRoad>,
    override val startingRoads: List<NagelRoad>,
    override val phases: Map<Lane, TrafficLightPhase>
//    val position: Position
) : NagelRoadNode, Intersection {

    init {
        endingRoads.forEach { it.setEnd(this) }
    }

    override fun getPossibleRoads(lane: Lane): List<NagelRoad> {
        return directions.filter { it.from == lane }
            .map { it.to }
    }

    override fun getPossibleRoads(road: Road): List<NagelRoad> {
        return road.lanes.flatMap { getPossibleRoads(it) }
    }

    /**
     * Answers if Intersection can be entered from [lane],
     * depends on TrafficLightPhase assigned to that line
     */
    fun canGoThrough(lane: NagelLane): Boolean {
        return phases[lane]!!.state == TrafficLightPhase.LightColor.GREEN
    }

    override fun lightPhasesOf(road: Road): List<TrafficLightPhase> {
        return road.lanes.map { phases[it]!! }
    }

    override fun toString(): String {
        return "NagelIntersection(phases=$phases)"
    }
}
