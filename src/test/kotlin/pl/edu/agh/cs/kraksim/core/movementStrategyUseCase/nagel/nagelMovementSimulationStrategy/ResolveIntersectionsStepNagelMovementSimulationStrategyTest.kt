package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.nagelMovementSimulationStrategy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.*
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelCar

internal class ResolveIntersectionsStepNagelMovementSimulationStrategyTest {

    @Test
    fun `Given car in front of intersection with distance left to move, when resolve intersection move to destination lane`() {
        // given
        val expectedPosition = 1
        val state = getTwoRoadConnectedWithIntersectionSimulationState()
        val expectedLane = state.getFirstLane(roadId = 1)
        val car = NagelCar(
            velocity = 2,
            gps = GPS(state.road(1), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car.moveToLaneFront(state.getFirstLane(), newPosition = 3)
        car.distanceLeftToMove = 2

        val strategy = testNagelMovementSimulationStrategy()

        // when
        strategy.resolveIntersections(state)

        // then
        assertThat(car.currentLane).isEqualTo(expectedLane)
        assertThat(car.positionRelativeToStart).isEqualTo(expectedPosition)
    }

    @Test
    fun `Given car in front of intersection with no distance left to move, when resolve intersection don't change position`() {
        // given
        val expectedPosition = 3
        val state = getTwoRoadConnectedWithIntersectionSimulationState()
        val expectedLane = state.getFirstLane(roadId = 0)
        val car = NagelCar(
            velocity = 2,
            gps = mockGps()
        )
        car.moveToLaneFront(state.getFirstLane(), newPosition = 3)
        car.distanceLeftToMove = 0
        val strategy = testNagelMovementSimulationStrategy()

        // when
        strategy.resolveIntersections(state)

        // then
        assertThat(car.currentLane).isEqualTo(expectedLane)
        assertThat(car.positionRelativeToStart).isEqualTo(expectedPosition)
    }

    @Test
    fun `Given T-intersection with 2 cars merging into same lane when resolve intersection position them correctly`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val destinationLane = state.getFirstLane(roadId = 2)
        val expectedCar1Position = 1
        val expectedCar2Position = 0

        val car1 = NagelCar(
            velocity = 2,
            gps = GPS(state.road(2), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car1.moveToLaneFront(state.getFirstLane(roadId = 0), newPosition = 3)
        car1.distanceLeftToMove = 2

        val car2 = NagelCar(
            velocity = 2,
            gps = GPS(state.road(2), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car2.moveToLaneFront(state.getFirstLane(roadId = 1), newPosition = 3)
        car2.distanceLeftToMove = 1

        val strategy = testNagelMovementSimulationStrategy()

        // when
        strategy.resolveIntersections(state)

        // then
        assertThat(car1.currentLane).isEqualTo(destinationLane)
        assertThat(car2.currentLane).isEqualTo(destinationLane)
        assertThat(car1.positionRelativeToStart).isEqualTo(expectedCar1Position)
        assertThat(car2.positionRelativeToStart).isEqualTo(expectedCar2Position)
    }

    @Test
    fun `Given T-intersection with 2 cars merging into same lane having same distance left to move when resolve intersection position them correctly`() {
        // given
        val state = getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState()
        val destinationLane = state.getFirstLane(roadId = 2)
        val expectedOneOfTheCarsPosition = 1
        val expectedSecondOfTheCarsPosition = 0

        val car1 = NagelCar(
            velocity = 2,
            gps = GPS(state.road(2), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car1.moveToLaneFront(state.getFirstLane(roadId = 0), newPosition = 3)
        car1.distanceLeftToMove = 2

        val car2 = NagelCar(
            velocity = 2,
            gps = GPS(state.road(2), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        car2.moveToLaneFront(state.getFirstLane(roadId = 1), newPosition = 3)
        car2.distanceLeftToMove = 2

        val strategy = testNagelMovementSimulationStrategy()

        // when
        strategy.resolveIntersections(state)

        // then
        assertThat(car1.currentLane).isEqualTo(destinationLane)
        assertThat(car2.currentLane).isEqualTo(destinationLane)
        assertThat(car1.positionRelativeToStart).isIn(expectedOneOfTheCarsPosition, expectedSecondOfTheCarsPosition)
        assertThat(car2.positionRelativeToStart).isIn(expectedOneOfTheCarsPosition, expectedSecondOfTheCarsPosition)
    }
}
