package pl.edu.agh.cs.kraksim.core.state

import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.StateType

interface SimulationState {
    val id: Long
    var turn: Long

    val roads: Map<RoadId, Road>
    val gateways: Map<GatewayId, Gateway>
    val intersections: Map<IntersectionId, Intersection>

    val lanes: List<Lane>
    val cars: List<Car>

    val type: StateType

    var finished: Boolean
}
