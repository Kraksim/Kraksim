package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state

import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.core.state.IntersectionTurningLaneDirection
import pl.edu.agh.cs.kraksim.core.state.Lane
import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase.LightColor.GREEN

class NagelIntersection(
    override val id: Long,
    override val directions: List<IntersectionTurningLaneDirection>,
    endingRoads: List<NagelRoad>,
    startingRoads: List<NagelRoad>,
    override val phases: Map<LaneId, TrafficLightPhase>,
    override val name: String
) : NagelRoadNode, Intersection {

    override val startingRoads: Map<RoadId, NagelRoad> = startingRoads.associateBy { it.id }
    override val endingRoads: Map<RoadId, NagelRoad> = endingRoads.associateBy { it.id }

    init {
        this.endingRoads.values.forEach { it.end = this }
    }

    override fun getPossibleRoads(lane: Lane): List<NagelRoad> {
        return directions.filter { it.from == lane.id }
            .map { startingRoads[it.to]!! }
    }

    override fun getPossibleRoads(road: Road): List<NagelRoad> {
        return road.lanes.flatMap { getPossibleRoads(it) }
    }

    override fun getLanesLeadingTo(road: RoadId): Set<LaneId> {
        return directions.filter { it.to == road }.map { it.from }.toSet()
    }

    /**
     * Answers if Intersection can be entered from [lane],
     * depends on TrafficLightPhase assigned to that line
     */
    fun canGoThrough(lane: NagelLane): Boolean {
        val phaseForLane = phases[lane.id] ?: return false
        return phaseForLane.state == GREEN
    }

    override fun lightPhasesOf(road: Road): List<TrafficLightPhase> {
        return road.lanes.map { phases[it.id]!! }
    }

    override fun toString(): String {
        return "NagelIntersection(phases=$phases)"
    }
}
