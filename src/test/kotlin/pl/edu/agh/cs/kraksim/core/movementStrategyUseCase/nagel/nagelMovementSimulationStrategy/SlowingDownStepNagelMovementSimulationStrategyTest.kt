package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.nagelMovementSimulationStrategy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.*
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelCar
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase.LightColor

internal class SlowingDownStepNagelMovementSimulationStrategyTest {

    @Test
    fun `Given one car in lane, when slowing down stage don't change velocity`() {
        // given
        val initialVelocity = 4
        val expectedVelocity = 4
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(0), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car.moveToLane(state.getLane(), 0)
        val strategy = testNagelMovementSimulationStrategy()

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
            gps = GPS(state.road(0), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        frontCar.moveToLane(state.getLane(), 2)
        val backCar = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(0), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        backCar.moveToLane(state.getLane(), 1)
        val strategy = testNagelMovementSimulationStrategy()

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
            gps = GPS(state.road(0), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        frontCar.moveToLane(state.getLane(), 5)
        val backCar = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(0), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        backCar.moveToLane(state.getLane(), 2)
        val strategy = testNagelMovementSimulationStrategy()

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
            firstRoadLength = 20,
            secondRoadLength = 80
        )
        val car = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(1), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car.moveToLane(state.getLane(), 3)
        val strategy = testNagelMovementSimulationStrategy()

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
            firstRoadLength = 20,
            secondRoadLength = 80,
            trafficLightColor = LightColor.RED
        )
        val car = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(1), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car.moveToLane(state.getLane(), 3)
        val strategy = testNagelMovementSimulationStrategy()

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
            firstRoadLength = 20,
            secondRoadLength = 80,
            trafficLightColor = LightColor.RED
        )
        val car = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(1), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car.moveToLane(state.getLane(), 1)
        val strategy = testNagelMovementSimulationStrategy()

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
            firstRoadLength = 20,
        )
        val car1 = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(1), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car1.moveToLane(state.getLane(), 3)
        val car2 = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(1), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car2.moveToLane(state.getLane(roadId = 1), 0)
        val strategy = testNagelMovementSimulationStrategy()

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
            firstRoadLength = 20,
        )
        val car1 = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(1), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car1.moveToLane(state.getLane(), 3)
        val car2 = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(1), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car2.moveToLane(state.getLane(roadId = 1), 1)
        val strategy = testNagelMovementSimulationStrategy()

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
            firstRoadLength = 20,
        )
        val car1 = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(1), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car1.moveToLane(state.getLane(), 2)
        val car2 = NagelCar(
            velocity = initialVelocity,
            gps = GPS(state.road(1), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car2.moveToLane(state.getLane(roadId = 1), 1)
        val strategy = testNagelMovementSimulationStrategy()

        // when
        strategy.slowingDown(state)

        // then
        assertThat(car1.velocity).isEqualTo(expectedVelocity)
    }
}
