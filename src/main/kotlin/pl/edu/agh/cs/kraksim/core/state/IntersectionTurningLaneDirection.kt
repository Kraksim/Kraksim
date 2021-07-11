package pl.edu.agh.cs.kraksim.core.state

import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId

data class IntersectionTurningLaneDirection(
    val from: LaneId,
    val to: RoadId
)
