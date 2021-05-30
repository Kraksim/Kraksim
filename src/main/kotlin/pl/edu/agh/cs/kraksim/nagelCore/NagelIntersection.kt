package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.comon.Position
import pl.edu.agh.cs.kraksim.comon.TrafficLightPhase

class NagelIntersection(
    val id: Long,
    val directions: Set<NagelIntersectionTurningLaneDirection>,
    val phases: Map<NagelLane, TrafficLightPhase>,
    val connectedRoads: List<NagelRoad>,
    val position: Position
) : NagelRoadNode {

    fun getPossibleRoads(lane: NagelLane): List<NagelRoad> {
        return directions.filter { it.from == lane }
            .map { it.to }
    }

    override fun canEnterNodeFrom(lane: NagelLane): Boolean {
        return phases[lane]!!.state == TrafficLightPhase.LightColor.GREEN
    }
}