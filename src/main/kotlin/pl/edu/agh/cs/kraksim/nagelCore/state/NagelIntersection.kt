package pl.edu.agh.cs.kraksim.nagelCore.state

import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.core.state.IntersectionTurningLaneDirection
import pl.edu.agh.cs.kraksim.core.state.Lane
import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase

class NagelIntersection(
        override val id: Long,
        val directions: List<IntersectionTurningLaneDirection>,
        endingRoads: List<NagelRoad>,
        startingRoads: List<NagelRoad>,
        override val phases: Map<LaneId, TrafficLightPhase>
//    val position: Position
) : NagelRoadNode, Intersection {

    override val startingRoads: Map<RoadId, NagelRoad> = startingRoads.associateBy { it.id }
    override val endingRoads: Map<RoadId, NagelRoad> = endingRoads.associateBy { it.id }

    init {
        this.endingRoads.values.forEach { it.end = this }
    }

    override fun getPossibleRoads(lane: Lane): List<NagelRoad> {
        return directions.filter { it.from == lane.id }
            .map { startingRoads[it.to]!! }
    }

    override fun getPossibleRoads(road: Road): List<NagelRoad> {
        return road.lanes.flatMap { getPossibleRoads(it) }
    }

    /**
     * Answers if Intersection can be entered from [lane],
     * depends on TrafficLightPhase assigned to that line
     */
    fun canGoThrough(lane: NagelLane): Boolean {
        return phases[lane.id]!!.state == TrafficLightPhase.LightColor.GREEN
    }

    override fun lightPhasesOf(road: Road): List<TrafficLightPhase> {
        return road.lanes.map { phases[it.id]!! }
    }

    override fun toString(): String {
        return "NagelIntersection(phases=$phases)"
    }
}
