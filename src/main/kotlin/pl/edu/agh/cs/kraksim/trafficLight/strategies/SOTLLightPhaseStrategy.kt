package pl.edu.agh.cs.kraksim.trafficLight.strategies

import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseStrategy
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor.GREEN
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor.RED
import kotlin.math.max

class SOTLLightPhaseStrategy(
    private val phiFactor: Double = 10.0,
    private val minPhaseLength: Int = 2,
    // TODO("Rename those")
    private val omegaMin: Int = 10,
    private val ni: Int = 5
) : LightPhaseStrategy {

    /*
     * Initialize all lights as red since SOTL switches to green based on amount of cars on a given lane
     */
    override fun initializeLights(intersections: List<Intersection>) {
        intersections.forEach { initializeLights(it) }
    }

    private fun initializeLights(intersection: Intersection) {
        intersection.getLightPhasesOfLanesGroupedByRoad().forEach { phases ->
            phases.forEach { phase ->
                phase.phaseTime = 0
                phase.state = RED
            }
        }
    }

    override fun switchLights(intersections: List<Intersection>) {
        // increment red, decrement green
        intersections.forEach {
            it.getLightPhasesOfLanesGroupedByRoad()
                .onEach { lanes ->
                    lanes.forEach { phase ->
                        when (phase.state) {
                            GREEN -> phase.phaseTime--
                            RED -> phase.phaseTime++
                        }
                    }
                }
        }
        intersections.forEach { switchLights(it) }
    }

    private fun switchLights(intersection: Intersection) {
        intersection.phases.forEach { (lane, phase) ->
            when (phase.state) {
                RED -> {
                    val duration = phase.phaseTime
                    val carsCount = lane.cars.count()
                    if (duration * carsCount >= phiFactor && minPhaseLength <= duration) {
                        phase.state = GREEN
                        // calculate green light length
                        val lastCarPosition = lane.cars
                            .minByOrNull { it.positionRelativeToStart }?.positionRelativeToStart ?: 0
                        val lengthToEnd = lane.physicalLength - lastCarPosition
                        // all cars should go through this green light, so we can limit this by 1 * length to beat, although this might be too big value, so //TODO think about this
                        phase.phaseTime = max(minPhaseLength, lengthToEnd)
                    }
                }
                GREEN -> {
                    if (phase.phaseTime == 0) {
                        phase.state = RED
                        phase.phaseTime = 0
                    }
                }
            }
        }
    }
}
