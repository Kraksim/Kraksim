package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.core.RoadNode

interface NagelRoadNode : RoadNode {
    val endingRoads: List<NagelRoad>
    val startingRoads: List<NagelRoad>
}
