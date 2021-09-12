package pl.edu.agh.cs.kraksim.core.state

import pl.edu.agh.cs.kraksim.common.RoadId

interface RoadNode {
    val id: Long
    val endingRoads: Map<RoadId, Road>
    val startingRoads: Map<RoadId, Road>
}
