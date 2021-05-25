package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.comon.Position

class Intersection(
    val id: Long,
    val directions: Set<IntersectionTurningLaneDirection>,
    val phases: Map<NagelLane, TrafficLightPhase>,
    val connectedRoads: List<Road>,
    val position: Position
) : RoadNode {

    override fun getPossibleRoads(lane: NagelLane): Collection<Road> {
        return directions.filter { it.from == lane }
            .map { it.to }
    }
}