package pl.edu.agh.cs.kraksim.core.state

interface RoadNode {
    val id: Long
    val endingRoads: List<Road>
    val startingRoads: List<Road>
}
