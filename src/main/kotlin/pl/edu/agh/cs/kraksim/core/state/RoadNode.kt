package pl.edu.agh.cs.kraksim.core.state

import pl.edu.agh.cs.kraksim.common.RoadId

interface RoadNode {
    val id: Long
    val endingRoads: List<Road>
    val startingRoads: Map<RoadId, Road>
}
