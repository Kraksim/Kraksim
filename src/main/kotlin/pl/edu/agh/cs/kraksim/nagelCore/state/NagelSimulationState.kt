package pl.edu.agh.cs.kraksim.nagelCore.state

import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.core.state.SimulationState

class NagelSimulationState(
    override val id: Long,
    roadList: List<NagelRoad>,
    gatewayList: List<NagelGateway>,
    intersectionList: List<NagelIntersection>,
    override var turn: Long
) : SimulationState {

    override val roads: Map<RoadId, NagelRoad> = roadList.associateBy { it.id }
    override val gateways: Map<GatewayId, NagelGateway> = gatewayList.associateBy { it.id }
    override val intersections: Map<IntersectionId, NagelIntersection> = intersectionList.associateBy { it.id }

    override val lanes: List<NagelLane>
        get() = roads.flatMap { it.value.lanes }

    override val cars: List<NagelCar>
        get() = lanes.flatMap { it.cars }

    override fun toString(): String {
        return "NagelSimulationState(\n\tgateways=$gateways,\n\troads=$roads,\n\tintersections=$intersections)"
    }
}
