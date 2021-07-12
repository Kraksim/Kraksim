package pl.edu.agh.cs.kraksim.trafficLight

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.core.state.SimulationState

class LightPhaseManager(
    simulationState: SimulationState,
    strategies: Map<LightPhaseStrategy, List<IntersectionId>>
) {

    private val lightStrategyGroups: List<LightStrategyGroup> = strategies.map { (strategy, intersectionIds) ->
        val intersections = intersectionIds.map { id -> simulationState.intersections[id]!! }
        LightStrategyGroup(strategy, intersections)
    }.onEach { (lightPhaseStrategy, intersections) ->
        lightPhaseStrategy.initializeLights(intersections)
    }

    fun changeLights() {
        lightStrategyGroups.forEach { (lightPhaseStrategy, intersections) ->
            lightPhaseStrategy.switchLight(intersections)
        }
    }
}

data class LightStrategyGroup(
    val lightPhaseStrategy: LightPhaseStrategy,
    val intersections: List<Intersection>
)
