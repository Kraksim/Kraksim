package pl.edu.agh.cs.kraksim.nagelCore.nagelMovementSimulationStrategy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.MockRandomProvider
import pl.edu.agh.cs.kraksim.nagelCore.NagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelCar
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor

internal class SlowingDownStepNagelMovementSimulationStrategyTest {

    @Test
    fun `Given one car in lane, when slowing down stage don't change velocity`() {
        // given
        val initialVelocity = 4
        val expectedVelocity = 4
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
        )
        car.moveToLane(state.roads[0].lanes[0], 0)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.slowingDown(state)

        // then
        assertThat(car.velocity).isEqualTo(expectedVelocity)
    }

    @Test
    fun `Given two cars directly next to each other in lane, when slowing down stage change one velocity`() {
        // given
        val initialVelocity = 2
        val expectedBackCarVelocity = 0
        val state = getOneRoadSimulationState()
        val frontCar = NagelCar(
            velocity = initialVelocity,
        )
        frontCar.moveToLane(state.getLine(), 2)
        val backCar = NagelCar(
            velocity = initialVelocity,
        )
        backCar.moveToLane(state.getLine(), 1)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.slowingDown(state)

        // then
        assertThat(frontCar.velocity).isEqualTo(initialVelocity)
        assertThat(backCar.velocity).isEqualTo(expectedBackCarVelocity)
    }

    @Test
    fun `Given two cars in some distance to each other in lane, when slowing down stage change one velocity`() {
        // given
        val initialVelocity = 4
        val expectedBackCarVelocity = 2
        val state = getOneRoadSimulationState()
        val frontCar = NagelCar(
            velocity = initialVelocity,
        )
        frontCar.moveToLane(state.getLine(), 5)
        val backCar = NagelCar(
            velocity = initialVelocity,
        )
        backCar.moveToLane(state.getLine(), 2)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.slowingDown(state)

        // then
        assertThat(frontCar.velocity).isEqualTo(initialVelocity)
        assertThat(backCar.velocity).isEqualTo(expectedBackCarVelocity)
    }

    @Test
    fun `Given car in front of intersection and destination lane empty, when slowing down don't change velocity`() {
        // given
        val initialVelocity = 6
        val expectedVelocity = 6
        val state = getTwoRoadConnectedWithIntersectionSimulationState(
            firstRoadLength = 18,
            secondRoadLength = 72
        )
        val car = NagelCar(
            velocity = initialVelocity,
        )
        car.moveToLane(state.getLine(), 3)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.slowingDown(state)

        // then
        assertThat(car.velocity).isEqualTo(expectedVelocity)
    }

    @Test
    fun `Given car in front of intersection with red traffic light and next lane empty, when slowing down decrease velocity`() {
        // given
        val initialVelocity = 6
        val expectedVelocity = 0
        val state = getTwoRoadConnectedWithIntersectionSimulationState(
            firstRoadLength = 18,
            secondRoadLength = 72,
            trafficLightColor = LightColor.RED
        )
        val car = NagelCar(
            velocity = initialVelocity,
        )
        car.moveToLane(state.getLine(), 3)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.slowingDown(state)

        // then
        assertThat(car.velocity).isEqualTo(expectedVelocity)
    }

    @Test
    fun `Given car in some distance to intersection with red traffic light and next lane empty, when slowing down decrease velocity just to get to intersection`() {
        // given
        val initialVelocity = 6
        val expectedVelocity = 2
        val state = getTwoRoadConnectedWithIntersectionSimulationState(
            firstRoadLength = 18,
            secondRoadLength = 72,
            trafficLightColor = LightColor.RED
        )
        val car = NagelCar(
            velocity = initialVelocity,
        )
        car.moveToLane(state.getLine(), 1)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.slowingDown(state)

        // then
        assertThat(car.velocity).isEqualTo(expectedVelocity)
    }

    @Test
    fun `Given car in front of intersection and car in beginning of next lane when slowing down decrease velocity`() {
        // given
        val initialVelocity = 1
        val expectedVelocity = 0
        val state = getTwoRoadConnectedWithIntersectionSimulationState(
            firstRoadLength = 18,
        )
        val car1 = NagelCar(
            velocity = initialVelocity,
        )
        car1.moveToLane(state.getLine(), 3)
        val car2 = NagelCar(
            velocity = initialVelocity,
        )
        car2.moveToLane(state.getLine(road = 1), 0)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.slowingDown(state)

        // then
        assertThat(car1.velocity).isEqualTo(expectedVelocity)
    }

    @Test
    fun `Given car in front of intersection and car in middle of next lane when slowing down decrease velocity`() {
        // given
        val initialVelocity = 2
        val expectedVelocity = 1
        val state = getTwoRoadConnectedWithIntersectionSimulationState(
            firstRoadLength = 18,
        )
        val car1 = NagelCar(
            velocity = initialVelocity,
        )
        car1.moveToLane(state.getLine(), 3)
        val car2 = NagelCar(
            velocity = initialVelocity,
        )
        car2.moveToLane(state.getLine(road = 1), 1)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.slowingDown(state)

        // then
        assertThat(car1.velocity).isEqualTo(expectedVelocity)
    }

    @Test
    fun `Given car 1 cell in front of intersection and car 1 cell after start of next lane when slowing down decrease velocity`() {
        // given
        val initialVelocity = 4
        val expectedVelocity = 2
        val state = getTwoRoadConnectedWithIntersectionSimulationState(
            firstRoadLength = 18,
        )
        val car1 = NagelCar(
            velocity = initialVelocity,
        )
        car1.moveToLane(state.getLine(), 2)
        val car2 = NagelCar(
            velocity = initialVelocity,
        )
        car2.moveToLane(state.getLine(road = 1), 1)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.slowingDown(state)

        // then
        assertThat(car1.velocity).isEqualTo(expectedVelocity)
    }
}
