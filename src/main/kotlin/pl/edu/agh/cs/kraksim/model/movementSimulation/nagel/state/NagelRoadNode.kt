package pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.state

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.RoadNode

interface NagelRoadNode : RoadNode {
    override val endingRoads: Map<RoadId, NagelRoad>
    override val startingRoads: Map<RoadId, NagelRoad>
}
