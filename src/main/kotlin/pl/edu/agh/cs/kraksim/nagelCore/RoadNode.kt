package pl.edu.agh.cs.kraksim.nagelCore

interface RoadNode {
    fun getPossibleRoads(lane: NagelLane): Collection<Road>
}