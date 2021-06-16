package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.common.TrafficLightPhase
import pl.edu.agh.cs.kraksim.core.Intersection

class NagelIntersection(
    override val id: Long,
    val directions: Set<NagelIntersectionTurningLaneDirection>,
    override val endingRoads: List<NagelRoad>,
    override val startingRoads: List<NagelRoad>,
    var phases: Map<NagelLane, TrafficLightPhase> = emptyMap()
//    val position: Position
) : NagelRoadNode, Intersection {

    init {
        endingRoads.forEach { it.setEnd(this) }
    }

    fun getPossibleRoads(lane: NagelLane): List<NagelRoad> {
        return directions.filter { it.from == lane }
            .map { it.to }
    }

    /**
     * Answers if Intersection can be entered from [lane],
     * depends on TrafficLightPhase assigned to that line
     */
    fun canGoThrough(lane: NagelLane): Boolean {
        return phases[lane]!!.state == TrafficLightPhase.LightColor.GREEN
    }
}
