package pl.edu.agh.cs.kraksim.trafficLight.application.strategy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.getFirstLane
import pl.edu.agh.cs.kraksim.common.getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase.LightColor.GREEN
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase.LightColor.RED

internal class TurnBasedLightPhaseStrategyTest {

    @Test
    fun `Given state with not initialized intersection lights, initialize them correctly`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = TurnBasedLightPhaseStrategy()
        val intersection = state.intersections[0]!!
        val lane1 = state.getFirstLane()
        val lane2 = state.getFirstLane(roadId = 1)
        val phases = intersection.phases

        // when
        strategy.initializeLights(state.intersections.values)

        // then
        assertThat(phases[lane1.id]?.period).isEqualTo(5)
        assertThat(phases[lane1.id]?.state).isEqualTo(GREEN)
        assertThat(phases[lane2.id]?.period).isEqualTo(5)
        assertThat(phases[lane2.id]?.state).isEqualTo(RED)
    }

    @Test
    fun `Given state with initialized intersection lights, when switch light increase phase time`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = TurnBasedLightPhaseStrategy()
        val intersection = state.intersections[0]!!
        val lane1 = state.getFirstLane()
        val lane2 = state.getFirstLane(roadId = 1)
        val phases = intersection.phases
        strategy.initializeLights(state.intersections.values)

        // when
        strategy.switchLights(state.intersections.values)

        // then
        assertThat(phases[lane1.id]?.phaseTime).isEqualTo(1)
        assertThat(phases[lane1.id]?.state).isEqualTo(GREEN)
        assertThat(phases[lane2.id]?.phaseTime).isEqualTo(1)
        assertThat(phases[lane2.id]?.state).isEqualTo(RED)
    }

    @Test
    fun `Given state with intersection with green light phase about to end, when switch light decrease red lights, change green to red and lowest red to green`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = TurnBasedLightPhaseStrategy()
        val intersection = state.intersections[0]!!
        val lane1 = state.getFirstLane()
        val lane2 = state.getFirstLane(roadId = 1)
        val phases = intersection.phases
        val lane1LightPhase = phases[lane1.id]
        val lane2LightPhase = phases[lane2.id]
        lane1LightPhase?.phaseTime = 4
        lane1LightPhase?.state = GREEN
        lane1LightPhase?.period = 5

        lane2LightPhase?.phaseTime = 4
        lane2LightPhase?.state = RED
        lane2LightPhase?.period = 5

        // when
        strategy.switchLights(state.intersections.values)

        // then
        assertThat(lane1LightPhase?.phaseTime).isEqualTo(0)
        assertThat(lane1LightPhase?.state).isEqualTo(RED)
        assertThat(lane2LightPhase?.phaseTime).isEqualTo(0)
        assertThat(lane2LightPhase?.state).isEqualTo(GREEN)
    }
}
