package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.core.SimulationState

class NagelSimulationState(
    override var gateways: List<NagelGateway>,
    override var roads: List<NagelRoad>,
    override var intersections: List<NagelIntersection>
) : SimulationState {


    override fun toString(): String {
        return "NagelSimulationState(\n\tgateways=$gateways,\n\troads=$roads,\n\tintersections=$intersections)"
    }

}
