package pl.edu.agh.cs.kraksim.trafficLight.strategies

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.getLane
import pl.edu.agh.cs.kraksim.common.getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor.GREEN
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor.RED

internal class TurnBasedLightPhaseStrategyTest {

    @Test
    fun `Given state with not initialized intersection lights, initialize them correctly`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = TurnBasedLightPhaseStrategy()
        val intersection = state.intersections[0]
        val lane1 = state.getLane()
        val lane2 = state.getLane(roadId = 1)
        val phases = intersection.phases

        // when
        strategy.initializeLights(state.intersections)

        // then
        assertThat(phases[lane1]?.phaseTime).isEqualTo(5)
        assertThat(phases[lane1]?.state).isEqualTo(GREEN)
        assertThat(phases[lane2]?.phaseTime).isEqualTo(5)
        assertThat(phases[lane2]?.state).isEqualTo(RED)
    }

    @Test
    fun `Given state with initialized intersection lights, when switch light decrease phase time`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = TurnBasedLightPhaseStrategy()
        val intersection = state.intersections[0]
        val lane1 = state.getLane()
        val lane2 = state.getLane(roadId = 1)
        val phases = intersection.phases
        strategy.initializeLights(state.intersections)

        // when
        strategy.switchLight(state.intersections)

        // then
        assertThat(phases[lane1]?.phaseTime).isEqualTo(4)
        assertThat(phases[lane1]?.state).isEqualTo(GREEN)
        assertThat(phases[lane2]?.phaseTime).isEqualTo(4)
        assertThat(phases[lane2]?.state).isEqualTo(RED)
    }

    @Test
    fun `Given state with intersection with green light phase about to end, when switch light decrease red lights, change green to red and lowest red to green`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = TurnBasedLightPhaseStrategy()
        val intersection = state.intersections[0]
        val lane1 = state.getLane()
        val lane2 = state.getLane(roadId = 1)
        val phases = intersection.phases
        val lane1LightPhase = phases[lane1]
        val lane2LightPhase = phases[lane2]
        lane1LightPhase?.phaseTime = 1
        lane1LightPhase?.state = GREEN
        lane2LightPhase?.phaseTime = 1
        lane2LightPhase?.state = RED

        // when
        strategy.switchLight(state.intersections)

        // then
        assertThat(lane1LightPhase?.phaseTime).isEqualTo(5)
        assertThat(lane1LightPhase?.state).isEqualTo(RED)
        assertThat(lane2LightPhase?.phaseTime).isEqualTo(5)
        assertThat(lane2LightPhase?.state).isEqualTo(GREEN)
    }
}
