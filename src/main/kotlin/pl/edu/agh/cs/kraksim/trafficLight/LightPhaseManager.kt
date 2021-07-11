package pl.edu.agh.cs.kraksim.trafficLight

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.core.state.SimulationState

class LightPhaseManager(
    simulationState: SimulationState,
    strategies: Map<LightPhaseStrategyType, List<IntersectionId>>
) {

    private val lightStrategyGroups: List<LightStrategyGroup> = strategies.map { (type, intersectionIds) ->
        val intersections = intersectionIds.map { id -> simulationState.intersections[id]!! }
        LightStrategyGroup(type, intersections)
    }.onEach { (lightPhaseStrategy, intersections) ->
        lightPhaseStrategy.initializeLights(intersections)
    }

    fun changeLights() {
        lightStrategyGroups.forEach { (lightPhaseStrategy, intersections) ->
            lightPhaseStrategy.switchLight(intersections)
        }
    }
}
