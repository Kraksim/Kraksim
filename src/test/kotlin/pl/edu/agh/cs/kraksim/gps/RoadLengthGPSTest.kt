package pl.edu.agh.cs.kraksim.gps

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.edu.agh.cs.kraksim.common.OneLaneNagelStateBuilder
import pl.edu.agh.cs.kraksim.common.gateway
import pl.edu.agh.cs.kraksim.common.getOneRoadSimulationState
import pl.edu.agh.cs.kraksim.common.road
import pl.edu.agh.cs.kraksim.gps.algorithms.RoadLengthGPS

/**
 * Below are tests in different scenarios, each has a map drawn over it
 * `G(n)` or `I(n)` where n is a number means that it is a RoadNode with n being id of that node
 * arrows are directional roads connecting them, next to each one is a pair of numbers `m, o`,
 * m is id and o is length of that road.
 */
@SpringBootTest(classes = [RoadLengthGPS::class])
class RoadLengthGPSTest @Autowired constructor(
    private val algorithm: RoadLengthGPS
) {

    /*
               0,20        1,20
        G(10) -----> I(0) -----> G(11)
                       |
                       | 2,20
                       |
                       V    3,20
                      I(1) -----> G(12)
     */
    @Test
    fun `Given starting point in G(10) and target G(12), when RoadLengthGPS should find correct route`() {
        // given
        val state = OneLaneNagelStateBuilder(0..1, 10..12)
            .connect(sourceId = 10, destinationId = 0, length = 20, roadId = 0)
            .connect(sourceId = 0, destinationId = 11, length = 20, roadId = 1)
            .connect(sourceId = 0, destinationId = 1, length = 20, roadId = 2)
            .connect(sourceId = 1, destinationId = 12, length = 20, roadId = 3)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 2)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 1)
            .turnDirection(intersectionId = 1, sourceRoadId = 2, destinationRoadId = 3)
            .build()

        val expectedRoute = listOf(state.road(0), state.road(2), state.road(3))

        // when
        val gps = algorithm.calculate(state.gateway(10), state.gateway(12), state)

        // then
        assertThat(gps.route).isEqualTo(expectedRoute)
    }

    /*
               0,20        1,20
        G(10) -----> I(0) --------- G(11)
                       |        ┐
                2,20   |      /
                       |    / 3, 20
                       V  /
                      I(1)
     */
    @Test
    fun `Given starting point in G(10) and target G(11), when RoadLengthGPS should find shortest route`() {
        // given
        val state = OneLaneNagelStateBuilder(0..1, 10..11)
            .connect(sourceId = 10, destinationId = 0, length = 20, roadId = 0)
            .connect(sourceId = 0, destinationId = 11, length = 20, roadId = 1)
            .connect(sourceId = 0, destinationId = 1, length = 20, roadId = 2)
            .connect(sourceId = 1, destinationId = 11, length = 20, roadId = 3)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 1)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 2)
            .turnDirection(intersectionId = 1, sourceRoadId = 2, destinationRoadId = 3)
            .build()

        val expectedRoute = listOf(state.road(0), state.road(1))

        // when
        val gps = algorithm.calculate(state.gateway(10), state.gateway(11), state)

        // then
        assertThat(gps.route).isEqualTo(expectedRoute)
    }

    /*
              0,20        1,20
       G(10) -----> I(0) --------- G(11)
                      |        ┐
               2,20   |      /
                      |    / 3, 20
                      V  /
                     I(1)
    */
    @Test
    fun `Given starting point in G(10) and target G(11), but no turn direction leading to road(1), when RoadLengthGPS should find shortest route`() {
        // given
        val state = OneLaneNagelStateBuilder(0..1, 10..11)
            .connect(sourceId = 10, destinationId = 0, length = 20, roadId = 0)
            .connect(sourceId = 0, destinationId = 11, length = 20, roadId = 1)
            .connect(sourceId = 0, destinationId = 1, length = 20, roadId = 2)
            .connect(sourceId = 1, destinationId = 11, length = 20, roadId = 3)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 2)
            .turnDirection(intersectionId = 1, sourceRoadId = 2, destinationRoadId = 3)
            .build()

        val expectedRoute = listOf(state.road(0), state.road(2), state.road(3))

        // when
        val gps = algorithm.calculate(state.gateway(10), state.gateway(11), state)

        // then
        assertThat(gps.route).isEqualTo(expectedRoute)
    }

    /*
               0,20        1,200
        G(10) -----> I(0) --------- G(11)
                       |        ┐
                2,20   |      /
                       |    / 3, 20
                       V  /
                      I(1)
     */
    @Test
    fun `Given starting point in G(10) and target G(11) and long road connecting I(0) and G(11), when RoadLengthGPS should find shortest route by length`() {
        // given
        val state = OneLaneNagelStateBuilder(0..1, 10..11)
            .connect(sourceId = 10, destinationId = 0, length = 20, roadId = 0)
            .connect(sourceId = 0, destinationId = 11, length = 200, roadId = 1)
            .connect(sourceId = 0, destinationId = 1, length = 20, roadId = 2)
            .connect(sourceId = 1, destinationId = 11, length = 20, roadId = 3)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 1)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 2)
            .turnDirection(intersectionId = 1, sourceRoadId = 2, destinationRoadId = 3)
            .build()

        val expectedRoute = listOf(state.road(0), state.road(2), state.road(3))

        // when
        val gps = algorithm.calculate(state.gateway(10), state.gateway(11), state)

        // then
        assertThat(gps.route).isEqualTo(expectedRoute)
    }

    /*
              0,200        1,20
       G(10) -----> I(0) --------- G(11)
        \             ^        ┐
   4, 20  \      2,20 |      /
            \         |    / 3, 200
              \       |  /
                ---> I(1)
    */
    @Test
    fun `Given starting point in G(10) and target G(11) and some long roads, when RoadLengthGPS should find shortest route by length`() {
        // given
        val state = OneLaneNagelStateBuilder(0..1, 10..11)
            .connect(sourceId = 10, destinationId = 0, length = 200, roadId = 0)
            .connect(sourceId = 0, destinationId = 11, length = 20, roadId = 1)
            .connect(sourceId = 1, destinationId = 0, length = 20, roadId = 2)
            .connect(sourceId = 1, destinationId = 11, length = 200, roadId = 3)
            .connect(sourceId = 10, destinationId = 1, length = 20, roadId = 4)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 1)
            .turnDirection(intersectionId = 0, sourceRoadId = 2, destinationRoadId = 1)
            .turnDirection(intersectionId = 1, sourceRoadId = 4, destinationRoadId = 2)
            .turnDirection(intersectionId = 1, sourceRoadId = 4, destinationRoadId = 3)
            .build()

        val expectedRoute = listOf(state.road(4), state.road(2), state.road(1))

        // when
        val gps = algorithm.calculate(state.gateway(10), state.gateway(11), state)

        // then
        assertThat(gps.route).isEqualTo(expectedRoute)
    }

    /*
              0,20        1,20
       G(10) -----> I(0) -----> G(11)



                           3,20
                     I(1) -----> G(12)
    */
    @Test
    fun `Given target not reachable from source, throw exception`() {
        // given
        val state = OneLaneNagelStateBuilder(0..1, 10..12)
            .connect(sourceId = 10, destinationId = 0, length = 20, roadId = 0)
            .connect(sourceId = 0, destinationId = 11, length = 20, roadId = 1)
            .connect(sourceId = 1, destinationId = 12, length = 20, roadId = 3)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 1)
            .build()

        // when
        val gpsLambda: () -> Unit = {
            algorithm.calculate(state.gateway(10), state.gateway(12), state)
        }

        // then
        assertThatThrownBy(gpsLambda)
            .hasMessage("Target gateway (name=12, id=12) cannot be reached from source (name=10, id=10)")
    }

    /*
             0,20        1,20
      G(10) -----> I(0) -----> G(11)
   */
    @Test
    fun `Given target same as source, throw exception`() {
        // given
        val state = OneLaneNagelStateBuilder(0..0, 10..11)
            .connect(sourceId = 10, destinationId = 0, length = 20, roadId = 0)
            .connect(sourceId = 0, destinationId = 11, length = 20, roadId = 1)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 1)
            .build()

        // when
        val gpsLambda: () -> Unit = {
            algorithm.calculate(state.gateway(10), state.gateway(10), state)
        }

        // then
        assertThatThrownBy(gpsLambda)
            .hasMessage("GPS route target and source cannot be the same gateway (name=10, id=10)")
    }

    /*
           0,20
    G(10) -----> G(11)
 */
    @Test
    fun `Given target one road from source, calculate corectly`() {
        // given
        val state = getOneRoadSimulationState()
        val expectedRoute = listOf(state.road(0))

        // when
        val gps = algorithm.calculate(state.gateway(0), state.gateway(1), state)

        // then
        assertThat(gps.route).isEqualTo(expectedRoute)
    }
}
