package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.comon.TrafficLightPhase

class NagelIntersection(
    val id: Long,
    val directions: Set<NagelIntersectionTurningLaneDirection>,
    override val endingRoads: List<NagelRoad>,
    override val startingRoads: List<NagelRoad>,
//    val position: Position
) : NagelRoadNode {

    init {
        endingRoads.forEach { it.setEnd(this) }
    }

    var phases: Map<NagelLane, TrafficLightPhase> = emptyMap()

    fun getPossibleRoads(lane: NagelLane): List<NagelRoad> {
        return directions.filter { it.from == lane }
            .map { it.to }
    }

    override fun canEnterNodeFrom(lane: NagelLane): Boolean {
//        return phases[lane]!!.state == TrafficLightPhase.LightColor.GREEN
        return true
    }


}