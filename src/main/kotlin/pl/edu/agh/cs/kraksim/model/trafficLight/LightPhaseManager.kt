package pl.edu.agh.cs.kraksim.model.trafficLight

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.Intersection
import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.SimulationState

class LightPhaseManager(
    simulationState: SimulationState,
    public val strategies: Map<LightPhaseStrategy, List<IntersectionId>>
) {

    private val lightStrategyGroups: List<LightStrategyGroup> = strategies.map { (strategy, intersectionIds) ->
        val intersections = intersectionIds.map { id -> simulationState.intersections[id]!! }
        LightStrategyGroup(strategy, intersections)
    }.onEach { (lightPhaseStrategy, intersections) ->
        lightPhaseStrategy.initializeLights(intersections)
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
