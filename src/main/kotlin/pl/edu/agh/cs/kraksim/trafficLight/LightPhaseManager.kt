package pl.edu.agh.cs.kraksim.trafficLight

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.core.state.SimulationState

class LightPhaseManager(
    simulationState: SimulationState,
    strategies: Map<LightPhaseStrategyType, List<IntersectionId>>
) {

    private val lightStrategyGroups: List<LightStrategyGroup>

    init {
        val allIntersections = simulationState.intersections.associateBy { it.id }
        lightStrategyGroups = strategies.map { (type, intersectionIds) ->
            val intersections = intersectionIds.map { id -> allIntersections[id]!! }
            LightStrategyGroup(type, intersections)
        }.onEach { group ->
            group.lightPhaseStrategy.initializeLights(group.intersections)
        }
    }

    fun changeLights() {
        lightStrategyGroups.forEach { group ->
            group.lightPhaseStrategy.switchLight(group.intersections)
        }
    }
}
