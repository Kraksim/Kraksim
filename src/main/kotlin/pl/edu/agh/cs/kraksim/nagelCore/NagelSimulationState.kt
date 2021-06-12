package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.core.SimulationState

class NagelSimulationState(
    override val id: Long,
    override val roads: List<NagelRoad>,
    override val gateways: List<NagelGateway>,
    override val intersections: List<NagelIntersection>,
    override var turn: Long
) : SimulationState {

    override val lanes: List<NagelLane>
        get() = roads.flatMap { it.lanes }

    override val cars: List<NagelCar>
        get() = lanes.flatMap { it.cars }

    override fun toString(): String {
        return "NagelSimulationState(\n\tgateways=$gateways,\n\troads=$roads,\n\tintersections=$intersections)"
    }
}
