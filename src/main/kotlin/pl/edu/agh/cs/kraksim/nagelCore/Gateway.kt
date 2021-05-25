package pl.edu.agh.cs.kraksim.nagelCore

class Gateway(
    val id: Long,
    val connectedRoads: List<Road>
) : RoadNode {

    override fun getPossibleRoads(lane: NagelLane): Collection<Road> {
        return connectedRoads
    }
}