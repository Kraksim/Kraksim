package pl.edu.agh.cs.kraksim.trafficLight.strategies

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.createListOfCars
import pl.edu.agh.cs.kraksim.common.getLane
import pl.edu.agh.cs.kraksim.common.getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelCar
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.GPSType
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase

class SOTLLightPhaseStrategyTest {
    @Test
    fun `Given state with not initialized intersection lights, initialize them correctly`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = SOTLLightPhaseStrategy()
        val intersection = state.intersections[0]!!
        val lane1 = state.getLane()
        val lane2 = state.getLane(roadId = 1)
        val phases = intersection.phases
        // when
        strategy.initializeLights(state.intersections.values)

        // then
        assertThat(phases[lane1.id]?.phaseTime).isEqualTo(0)
        assertThat(phases[lane1.id]?.state).isEqualTo(TrafficLightPhase.LightColor.RED)
        assertThat(phases[lane2.id]?.phaseTime).isEqualTo(0)
        assertThat(phases[lane2.id]?.state).isEqualTo(TrafficLightPhase.LightColor.RED)
    }

    @Test
    fun `Given state with initialized intersection lights, when lights are switched increase red phaseTime`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = SOTLLightPhaseStrategy()
        val intersection = state.intersections[0]!!
        val lane1 = state.getLane()
        val lane2 = state.getLane(roadId = 1)
        val phases = intersection.phases
        strategy.initializeLights(state.intersections.values)

        // when
        strategy.switchLights(state.intersections.values)

        // then
        assertThat(phases[lane1.id]?.phaseTime).isEqualTo(1)
        assertThat(phases[lane1.id]?.state).isEqualTo(TrafficLightPhase.LightColor.RED)
        assertThat(phases[lane2.id]?.phaseTime).isEqualTo(1)
        assertThat(phases[lane2.id]?.state).isEqualTo(TrafficLightPhase.LightColor.RED)
    }

    /*
     * cars amount = 4, phi = 10 => phaseTime = 3 to change light to green, green duration: 18-0 = 18
     */
    @Test
    fun `Given state with initialized intersection and proper amount of cars in lane, switch lights to green with correct phaseTime value`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = SOTLLightPhaseStrategy()
        val intersection = state.intersections[0]!!
        val lane1 = state.getLane()
        val phases = intersection.phases
        val cars = createListOfCars(4, 3, 1)
        cars.forEach { lane1.addCar(it) }
        strategy.initializeLights(state.intersections.values)

        // when
        repeat(3) {
            strategy.switchLights(state.intersections.values)
        }
        // then
        assertThat(phases[lane1.id]?.state).isEqualTo(TrafficLightPhase.LightColor.GREEN)
        assertThat(phases[lane1.id]?.phaseTime).isEqualTo(18)
    }

    @Test
    fun `Given state with initialized intersection lights, when lights are switched decrease green phaseTime`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = SOTLLightPhaseStrategy()
        val intersection = state.intersections[0]!!
        val lane1 = state.getLane()
        val phases = intersection.phases
        val cars = createListOfCars(4, 3, 1)
        cars.forEach { lane1.addCar(it) }
        strategy.initializeLights(state.intersections.values)
        repeat(3) {
            strategy.switchLights(state.intersections.values)
        }

        // when
        repeat(4) {
            strategy.switchLights(state.intersections.values)
        }

        // then
        assertThat(phases[lane1.id]?.state).isEqualTo(TrafficLightPhase.LightColor.GREEN)
        assertThat(phases[lane1.id]?.phaseTime).isEqualTo(14)
    }

    @Test
    fun `Given state with initialized intersection and proper amount of cars in lane, switch lights to red with correct phaseTime value`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = SOTLLightPhaseStrategy()
        val intersection = state.intersections[0]!!
        val lane1 = state.getLane()
        val phases = intersection.phases
        val cars = createListOfCars(4, 3, 1)
        cars.forEach { lane1.addCar(it) }
        strategy.initializeLights(state.intersections.values)
        repeat(3) {
            strategy.switchLights(state.intersections.values)
        }
        // when
        repeat(18) {
            strategy.switchLights(state.intersections.values)
        }
        // then
        assertThat(phases[lane1.id]?.state).isEqualTo(TrafficLightPhase.LightColor.RED)
        assertThat(phases[lane1.id]?.phaseTime).isEqualTo(0)
    }

    @Test
    fun `Given state with initialized intersection and proper amount of cars in lane, switch lights to green with min phaseTime value`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val strategy = SOTLLightPhaseStrategy(minPhaseLength = 5, phiFactor = 5.0)
        val intersection = state.intersections[0]!!
        val lane1 = state.getLane()
        val phases = intersection.phases
        val lastCar = NagelCar(velocity = 3, gps = GPS(type = GPSType.DIJKSTRA_ROAD_LENGTH))
        lastCar.positionRelativeToStart = 17
        lane1.addCar(lastCar)
        strategy.initializeLights(state.intersections.values)

        // when
        repeat(5) {
            strategy.switchLights(state.intersections.values)
        }
        // then
        assertThat(phases[lane1.id]?.state).isEqualTo(TrafficLightPhase.LightColor.GREEN)
        assertThat(phases[lane1.id]?.phaseTime).isEqualTo(5)
    }
}
