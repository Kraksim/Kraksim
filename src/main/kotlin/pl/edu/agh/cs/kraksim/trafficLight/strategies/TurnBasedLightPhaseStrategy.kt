package pl.edu.agh.cs.kraksim.trafficLight.strategies

import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseStrategy
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor.GREEN
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor.RED

/**
 * Strategy on intersection switches lights to green for all lanes on one road only,
 * every green light lasts [turnLength]
 */
class TurnBasedLightPhaseStrategy(
    private val turnLength: Int = 5
) : LightPhaseStrategy {

    override fun initializeLights(intersections: List<Intersection>) {
        intersections.forEach { initializeLights(it) }
    }

    override fun switchLight(intersections: List<Intersection>) {
        intersections.forEach { switchLights(it) }
    }

    private fun initializeLights(intersection: Intersection) {
        intersection.getLightPhasesOfLanesGroupedByRoad()
            .forEachIndexed { index, phases ->
                phases.forEach { phase ->
                    phase.phaseTime = (index + 1) * turnLength
                    phase.state = if (index == 0) GREEN else RED
                }
            }
    }

    private fun switchLights(intersection: Intersection) {
        val roadsCount = intersection.endingRoads.size

        val (first: List<TrafficLightPhase>, second: List<TrafficLightPhase>) =
            intersection.getLightPhasesOfLanesGroupedByRoad()
                .onEach { lanes -> lanes.forEach { it.phaseTime-- } }
                .sortedBy { roadPhases -> roadPhases[0].phaseTime }

        if (first[0].phaseTime == 0) {
            first.forEach { it.switchLight(turnLength * roadsCount) }
            second.forEach { it.switchLight() }
        }
    }

    private fun Intersection.getLightPhasesOfLanesGroupedByRoad() =
        endingRoads.map { road -> lightPhasesOf(road) }
}
