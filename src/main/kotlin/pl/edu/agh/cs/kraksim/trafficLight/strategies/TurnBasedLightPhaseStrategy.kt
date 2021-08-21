package pl.edu.agh.cs.kraksim.trafficLight.strategies

import pl.edu.agh.cs.kraksim.common.split
import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseStrategy
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor.GREEN
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor.RED

/**
 * Strategy on intersection switches lights to green for all lanes on one road only,
 * every green light lasts [turnLength]
 */
class TurnBasedLightPhaseStrategy(
        private val turnLength: Int = 5,
        override val id: Long = 0
) : LightPhaseStrategy {

    override fun initializeLights(intersections: Collection<Intersection>) {
        intersections.forEach { initializeLights(it) }
    }

    override fun switchLights(intersections: Collection<Intersection>) {
        intersections.forEach { switchLights(it) }
    }

    /**
     * Arbitrarily takes one road in [intersection] and sets its light as green, for rest sets red and assign time accordingly
     */
    private fun initializeLights(intersection: Intersection) {
        val (head, tail) = intersection.getLightPhasesOfLanesGroupedByRoad().split()
        head.forEach { phase ->
            phase.phaseTime = turnLength
            phase.state = GREEN
        }

        tail.forEachIndexed { index, phases ->
            phases.forEach { phase ->
                phase.phaseTime = (index + 1) * turnLength
                phase.state = RED
            }
        }
    }

    private fun switchLights(intersection: Intersection) {
        val roadsCount = intersection.endingRoads.size

        val phasesToChange =
            intersection.getLightPhasesOfLanesGroupedByRoad()
                .onEach { lanes -> lanes.forEach { it.phaseTime-- } }
                .filter { roadPhases -> roadPhases[0].phaseTime == 0 }

        phasesToChange.forEach { lanePhases ->
            lanePhases.forEach { phase ->
                var phaseTime = turnLength

                // light that will be red after change should last turnLength + what longest red light lasts
                if (phase.state == GREEN) {
                    phaseTime *= roadsCount - 1 // minus one green light
                }
                phase.switchLight(phaseTime)
            }
        }
    }
}
