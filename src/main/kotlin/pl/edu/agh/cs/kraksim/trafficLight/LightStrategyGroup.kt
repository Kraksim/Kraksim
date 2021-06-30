package pl.edu.agh.cs.kraksim.trafficLight

import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.trafficLight.strategies.TurnBasedLightPhaseStrategy

data class LightStrategyGroup(
    val lightPhaseStrategy: LightPhaseStrategy,
    val intersections: List<Intersection>
) {
    constructor(type: LightPhaseStrategyType, intersections: List<Intersection>) : this(
        createLightPhaseStrategy(type),
        intersections
    )
}

enum class LightPhaseStrategyType {
    TURN_BASED
}

fun createLightPhaseStrategy(type: LightPhaseStrategyType): LightPhaseStrategy = when (type) {
    LightPhaseStrategyType.TURN_BASED -> TurnBasedLightPhaseStrategy()
}
