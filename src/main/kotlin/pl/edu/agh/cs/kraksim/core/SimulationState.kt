package pl.edu.agh.cs.kraksim.core

import pl.edu.agh.cs.kraksim.nagelCore.NagelGateway
import pl.edu.agh.cs.kraksim.nagelCore.NagelIntersection
import pl.edu.agh.cs.kraksim.nagelCore.NagelLane
import pl.edu.agh.cs.kraksim.nagelCore.NagelRoad

interface SimulationState {
    var gateways: List<NagelGateway>
    var roads: List<NagelRoad>
    var intersections: List<NagelIntersection>

    val lanes: List<NagelLane>
        get() = roads.flatMap { it.lanes }
}