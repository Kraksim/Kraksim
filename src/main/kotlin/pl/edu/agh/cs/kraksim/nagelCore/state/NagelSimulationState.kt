package pl.edu.agh.cs.kraksim.nagelCore.state

import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.trafficState.domain.StateType

class NagelSimulationState(
    override val id: Long,
    roads: List<NagelRoad>,
    gateways: List<NagelGateway>,
    intersections: List<NagelIntersection>,
    override var turn: Long
) : SimulationState {

    override val roads: Map<RoadId, NagelRoad> = roads.associateBy { it.id }
    override val gateways: Map<GatewayId, NagelGateway> = gateways.associateBy { it.id }
    override val intersections: Map<IntersectionId, NagelIntersection> = intersections.associateBy { it.id }

    override val lanes: List<NagelLane> = roads.flatMap { it.lanes }

    override val cars: List<NagelCar>
        get() = lanes.flatMap { it.cars }

    override fun toString(): String {
        return "NagelSimulationState(\n\tgateways=$gateways,\n\troads=$roads,\n\tintersections=$intersections)"
    }
    override val type: StateType = StateType.NAGEL_SCHRECKENBERG
}
