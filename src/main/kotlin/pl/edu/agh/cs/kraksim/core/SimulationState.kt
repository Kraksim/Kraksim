package pl.edu.agh.cs.kraksim.core

import pl.edu.agh.cs.kraksim.nagelCore.*

interface SimulationState {
    var gateways: List<NagelGateway>
    var roads: List<NagelRoad>
    var intersections: List<NagelIntersection>

    val lanes: List<NagelLane>
        get() = roads.flatMap { it.lanes }

    val cars: List<NagelCar>
        get() = lanes.flatMap { it.cars }
}