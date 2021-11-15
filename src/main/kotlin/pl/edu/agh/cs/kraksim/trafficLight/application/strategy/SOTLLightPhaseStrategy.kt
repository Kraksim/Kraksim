package pl.edu.agh.cs.kraksim.trafficLight.application.strategy

import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.core.state.Lane
import pl.edu.agh.cs.kraksim.trafficLight.application.LightPhaseStrategy
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase.LightColor.GREEN
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase.LightColor.RED
import kotlin.math.max

class SOTLLightPhaseStrategy(
    private val phiFactor: Double = 10.0,
    private val minPhaseLength: Int = 2,
    override val id: Long = 0
) : LightPhaseStrategy {

    /*
     * Initialize all lights as red since SOTL switches to green based on amount of cars on a given lane
     */
    override fun initializeLights(intersections: Collection<Intersection>) {
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

    override fun switchLights(intersections: Collection<Intersection>) {
        // increment both
        intersections.forEach {
            it.phases.forEach { (_, phase) ->
                phase.phaseTime++
            }
        }
        intersections.forEach { switchLights(it) }
    }

    private fun switchLights(intersection: Intersection) {
        val lanes = intersection.endingRoads.entries.flatMap { it.value.lanes }.associateBy { it.id }

        intersection.phases.forEach { (laneId: LaneId, phase) ->
            switchLight(phase, lanes[laneId])
        }
    }

    private fun switchLight(
        phase: TrafficLightPhase,
        lane: Lane?
    ) {
        requireNotNull(lane) { "Invalid road setup, expected not null road" }
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
                    // all cars should go through this green light, so we can limit this by 1 * length to beat
                    phase.phaseTime = 0
                    phase.period = max(minPhaseLength, lengthToEnd)
                }
            }
            GREEN -> {
                if (phase.phaseTime == phase.period) {
                    phase.state = RED
                    phase.phaseTime = 0
                }
            }
        }
    }
}
