package pl.edu.agh.cs.kraksim.nagelCore

class NagelGateway(
    val id: Long,
    val connectedRoads: List<NagelRoad>
) : NagelRoadNode {

    override fun getPossibleRoads(lane: NagelLane): Collection<NagelRoad> {
        return connectedRoads
    }

    override fun canEnterNodeFrom(lane: NagelLane): Boolean {
        return true
    }
}