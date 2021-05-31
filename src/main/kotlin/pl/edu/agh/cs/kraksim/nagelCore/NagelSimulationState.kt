package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.core.SimulationState

class NagelSimulationState(
    override var gateways: List<NagelGateway>,
    override var roads: List<NagelRoad>,
    override var intersections: List<NagelIntersection>
) : SimulationState {


    override fun toString(): String {
        return "NagelSimulationState(gateways=$gateways, roads=$roads, intersections=$intersections)"
    }

}