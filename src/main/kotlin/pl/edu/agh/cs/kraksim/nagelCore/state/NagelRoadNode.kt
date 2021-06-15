package pl.edu.agh.cs.kraksim.nagelCore.state

import pl.edu.agh.cs.kraksim.core.state.RoadNode

interface NagelRoadNode : RoadNode {
    override val endingRoads: List<NagelRoad>
    override val startingRoads: List<NagelRoad>
}
