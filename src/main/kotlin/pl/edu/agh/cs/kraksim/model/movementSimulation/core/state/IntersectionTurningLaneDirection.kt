package pl.edu.agh.cs.kraksim.model.movementSimulation.core.state

import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId

data class IntersectionTurningLaneDirection(
    val from: LaneId,
    val to: RoadId
)
