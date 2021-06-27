package pl.edu.agh.cs.kraksim.gps

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.OneLaneNagelStateBuilder
import pl.edu.agh.cs.kraksim.common.gateway
import pl.edu.agh.cs.kraksim.common.road

/**
 * Below are tests in different scenarios, each has a map drawn over it
 * `G(n)` or `I(n)` where n is a number means that it is a RoadNode with n being id of that node
 * arrows are directional roads connecting them, next to each one is a pair of numbers `m, o`,
 * m is id and o is length of that road.
 */
internal class RoadLengthGPSTest {

    /*
               0,18        1,18
        G(10) -----> I(0) -----> G(11)
                       |
                       | 2,18
                       |
                       V    3,18
                      I(1) -----> G(12)
     */
    @Test
    fun `Given starting point in G(10) and target G(12), when RoadLengthGPS should find correct route`() {
        // given
        val state = OneLaneNagelStateBuilder(0..1, 10..12)
            .connect(sourceId = 10, destinationId = 0, length = 18, roadId = 0)
            .connect(sourceId = 0, destinationId = 11, length = 18, roadId = 1)
            .connect(sourceId = 0, destinationId = 1, length = 18, roadId = 2)
            .connect(sourceId = 1, destinationId = 12, length = 18, roadId = 3)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 2)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 1)
            .turnDirection(intersectionId = 1, sourceRoadId = 2, destinationRoadId = 3)
            .build()

        val expectedRoute = listOf(state.road(0), state.road(2), state.road(3))

        // when
        val gps = RoadLengthGPS(state.gateway(10), state.gateway(12), state)

        // then
        Assertions.assertThat(gps.route).isEqualTo(expectedRoute)
    }

    /*
               0,18        1,18
        G(10) -----> I(0) --------- G(11)
                       |        ┐
                2,18   |      /
                       |    / 3, 18
                       V  /
                      I(1)
     */
    @Test
    fun `Given starting point in G(10) and target G(11), when RoadLengthGPS should find shortest route`() {
        // given
        val state = OneLaneNagelStateBuilder(0..1, 10..11)
            .connect(sourceId = 10, destinationId = 0, length = 18, roadId = 0)
            .connect(sourceId = 0, destinationId = 11, length = 18, roadId = 1)
            .connect(sourceId = 0, destinationId = 1, length = 18, roadId = 2)
            .connect(sourceId = 1, destinationId = 11, length = 18, roadId = 3)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 1)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 2)
            .turnDirection(intersectionId = 1, sourceRoadId = 2, destinationRoadId = 3)
            .build()

        val expectedRoute = listOf(state.road(0), state.road(1))

        // when
        val gps = RoadLengthGPS(state.gateway(10), state.gateway(11), state)

        // then
        Assertions.assertThat(gps.route).isEqualTo(expectedRoute)
    }

    /*
               0,18        1,180
        G(10) -----> I(0) --------- G(11)
                       |        ┐
                2,18   |      /
                       |    / 3, 18
                       V  /
                      I(1)
     */
    @Test
    fun `Given starting point in G(10) and target G(11) and long road connecting I(0) and G(11), when RoadLengthGPS should find shortest route by length`() {
        // given
        val state = OneLaneNagelStateBuilder(0..1, 10..11)
            .connect(sourceId = 10, destinationId = 0, length = 18, roadId = 0)
            .connect(sourceId = 0, destinationId = 11, length = 180, roadId = 1)
            .connect(sourceId = 0, destinationId = 1, length = 18, roadId = 2)
            .connect(sourceId = 1, destinationId = 11, length = 18, roadId = 3)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 1)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 2)
            .turnDirection(intersectionId = 1, sourceRoadId = 2, destinationRoadId = 3)
            .build()

        val expectedRoute = listOf(state.road(0), state.road(2), state.road(3))

        // when
        val gps = RoadLengthGPS(state.gateway(10), state.gateway(11), state)

        // then
        Assertions.assertThat(gps.route).isEqualTo(expectedRoute)
    }

    /*
              0,180        1,18
       G(10) -----> I(0) --------- G(11)
        \             ^        ┐
   4, 18  \      2,18 |      /
            \         |    / 3, 180
              \       |  /
                ---> I(1)
    */
    @Test
    fun `Given starting point in G(10) and target G(11) and some long roads, when RoadLengthGPS should find shortest route by length`() {
        // given
        val state = OneLaneNagelStateBuilder(0..1, 10..11)
            .connect(sourceId = 10, destinationId = 0, length = 180, roadId = 0)
            .connect(sourceId = 0, destinationId = 11, length = 18, roadId = 1)
            .connect(sourceId = 1, destinationId = 0, length = 18, roadId = 2)
            .connect(sourceId = 1, destinationId = 11, length = 180, roadId = 3)
            .connect(sourceId = 10, destinationId = 1, length = 18, roadId = 4)
            .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 1)
            .turnDirection(intersectionId = 0, sourceRoadId = 2, destinationRoadId = 1)
            .turnDirection(intersectionId = 1, sourceRoadId = 4, destinationRoadId = 2)
            .turnDirection(intersectionId = 1, sourceRoadId = 4, destinationRoadId = 3)
            .build()

        val expectedRoute = listOf(state.road(4), state.road(2), state.road(1))

        // when
        val gps = RoadLengthGPS(state.gateway(10), state.gateway(11), state)

        // then
        Assertions.assertThat(gps.route).isEqualTo(expectedRoute)
    }
}