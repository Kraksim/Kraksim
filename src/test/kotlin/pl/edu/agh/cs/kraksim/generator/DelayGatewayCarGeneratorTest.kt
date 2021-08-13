package pl.edu.agh.cs.kraksim.generator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.edu.agh.cs.kraksim.common.*
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.gps.GpsFactory
import pl.edu.agh.cs.kraksim.gps.algorithms.RoadLengthGPS

@SpringBootTest(classes = [DelayGatewayCarGenerator::class, GpsFactory::class, RoadLengthGPS::class])
internal class DelayGatewayCarGeneratorTest {

    @Autowired
    lateinit var generator: DelayGatewayCarGenerator

    @Test
    fun `Given gateway with generator, when generate put correct car`() {
        // given
        val state = getOneRoadSimulationState()
        val gateway = state.gateway(0)
        gateway.generators = listOf(
            Generator(
                lastCarReleasedTurnsAgo = 0,
                releaseDelay = 0,
                carsToRelease = 1,
                targetGatewayId = 1,
                gpsType = GPSType.DIJKSTRA_ROAD_LENGTH
            )
        )

        // when
        generator.generate(state)

        // then
        val cars = state.road(0).lanes[0].cars
        assertThat(cars.size).isEqualTo(1)
        cars[0].assertVelocity(0)
            .assertPositionRelativeToStart(0)
            .assertGpsRouteIsEmpty()
            .assertGpsType(GPSType.DIJKSTRA_ROAD_LENGTH)
    }

    @Test
    fun `Given gateway with generator with delay, when generate don't put car`() {
        // given
        val state = getOneRoadSimulationState()
        val gateway = state.gateway(0)
        gateway.generators = listOf(
            Generator(
                lastCarReleasedTurnsAgo = 0,
                releaseDelay = 1,
                carsToRelease = 1,
                targetGatewayId = 1,
                gpsType = GPSType.DIJKSTRA_ROAD_LENGTH
            )
        )

        // when
        generator.generate(state)

        // then
        val cars = state.road(0).lanes[0].cars
        assertThat(cars).isEmpty()
    }

    @Test
    fun `Given gateway with generator with delay, when generate 2 times put correct car and delete empty generator`() {
        // given
        val state = getOneRoadSimulationState()
        val gateway = state.gateway(0)
        gateway.generators = listOf(
            Generator(
                lastCarReleasedTurnsAgo = 0,
                releaseDelay = 1,
                carsToRelease = 1,
                targetGatewayId = 1,
                gpsType = GPSType.DIJKSTRA_ROAD_LENGTH
            )
        )

        // when
        generator.generate(state)
        generator.generate(state)

        // then
        val cars = state.road(0).lanes[0].cars
        assertThat(cars.size).isEqualTo(1)

        cars[0].assertVelocity(0)
            .assertPositionRelativeToStart(0)
            .assertGpsRouteIsEmpty()
            .assertGpsType(GPSType.DIJKSTRA_ROAD_LENGTH)

        assertThat(gateway.generators).isEmpty()
    }

    @Test
    fun `Given gateway with generators, when generate put car only from one`() {
        // given
        val state = getOneRoadSimulationState()
        val gateway = state.gateway(0)
        gateway.generators = listOf(
            Generator(
                lastCarReleasedTurnsAgo = 0,
                releaseDelay = 0,
                carsToRelease = 1,
                targetGatewayId = 1,
                gpsType = GPSType.DIJKSTRA_ROAD_LENGTH
            ),
            Generator(
                lastCarReleasedTurnsAgo = 0,
                releaseDelay = 0,
                carsToRelease = 1,
                targetGatewayId = 1,
                gpsType = GPSType.DIJKSTRA_ROAD_LENGTH
            )
        )

        // when
        generator.generate(state)

        // then
        val cars = state.road(0).lanes[0].cars
        assertThat(cars.size).isEqualTo(1)

        cars[0].assertVelocity(0)
            .assertPositionRelativeToStart(0)
            .assertGpsRouteIsEmpty()
            .assertGpsType(GPSType.DIJKSTRA_ROAD_LENGTH)

        assertThat(gateway.generators?.size).isEqualTo(1)
        assertThat(gateway.generators!![0]).isEqualTo(
            Generator(
                lastCarReleasedTurnsAgo = 1,
                releaseDelay = 0,
                carsToRelease = 1,
                targetGatewayId = 1,
                gpsType = GPSType.DIJKSTRA_ROAD_LENGTH
            )
        )
    }

    @Test
    fun `Given gateway with generator, when generate with no space, remove blocking car and generate then generate corectly`() {
        // given
        val state = getOneRoadSimulationState()
            .putCar(initialVelocity = 0, roadId = 0)
        val gateway = state.gateway(0)
        gateway.generators = listOf(
            Generator(
                lastCarReleasedTurnsAgo = 0,
                releaseDelay = 0,
                carsToRelease = 1,
                targetGatewayId = 1,
                gpsType = GPSType.DIJKSTRA_ROAD_LENGTH
            )
        )

        // when
        generator.generate(state)
        state.roads[0]!!.lanes[0].cars.clear()
        generator.generate(state)

        // then
        val cars = state.road(0).lanes[0].cars
        assertThat(cars.size).isEqualTo(1)

        cars[0].assertVelocity(0)
            .assertPositionRelativeToStart(0)
            .assertGpsRouteIsEmpty()
            .assertGpsType(GPSType.DIJKSTRA_ROAD_LENGTH)

        assertThat(gateway.generators).isEmpty()
    }
}
