package pl.edu.agh.cs.kraksim.trafficLight.application

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.core.state.SimulationState

class LightPhaseManager(
    simulationState: SimulationState,
    val strategies: Map<LightPhaseStrategy, List<IntersectionId>>
) {

    private val lightStrategyGroups: List<LightStrategyGroup> = strategies.map { (strategy, intersectionIds) ->
        LightStrategyGroup(
            lightPhaseStrategy = strategy,
            intersections = intersectionIds.map { id -> simulationState.intersections[id]!! }
        )
    }

    fun initializeLights() {
        lightStrategyGroups.forEach { (lightPhaseStrategy, intersections) ->
            lightPhaseStrategy.initializeLights(intersections)
        }
    }

    fun changeLights() {
        lightStrategyGroups.forEach { (lightPhaseStrategy, intersections) ->
            lightPhaseStrategy.switchLights(intersections)
        }
    }
}

data class LightStrategyGroup(
    val lightPhaseStrategy: LightPhaseStrategy,
    val intersections: List<Intersection>
)
