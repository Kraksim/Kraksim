package pl.edu.agh.cs.kraksim.nagelCore.simulation.nagelMovementSimulationStrategy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.MockRandomProvider
import pl.edu.agh.cs.kraksim.nagelCore.NagelCar
import pl.edu.agh.cs.kraksim.nagelCore.simulation.NagelMovementSimulationStrategy

internal class ResolveIntersectionsStepNagelMovementSimulationStrategyTest {

    @Test
    fun `Given car in front of intersection with distance left to move, when resolve intersection move to destination lane`() {
        // given
        val expectedPosition = 1
        val state = getTwoRoadConnectedWithIntersectionSimulationState()
        val expectedLane = state.roads[1].lanes[0]
        val car = NagelCar(velocity = 2)
        car.moveToLane(state.getLine(), newPosition = 3)
        car.distanceLeftToMove = 2
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

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
        val expectedLane = state.roads[0].lanes[0]
        val car = NagelCar(velocity = 2)
        car.moveToLane(state.getLine(), newPosition = 3)
        car.distanceLeftToMove = 0
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

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
        val destinationLane = state.roads[2].lanes[0]
        val expectedCar1Position = 1
        val expectedCar2Position = 0

        val car1 = NagelCar(velocity = 2)
        car1.moveToLane(state.getLine(road = 0), newPosition = 3)
        car1.distanceLeftToMove = 2

        val car2 = NagelCar(velocity = 2)
        car2.moveToLane(state.getLine(road = 1), newPosition = 3)
        car2.distanceLeftToMove = 1

        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

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
        val destinationLane = state.roads[2].lanes[0]
        val expectedOneOfTheCarsPosition = 1
        val expectedSecondOfTheCarsPosition = 0

        val car1 = NagelCar(velocity = 2)
        car1.moveToLane(state.getLine(road = 0), newPosition = 3)
        car1.distanceLeftToMove = 2

        val car2 = NagelCar(velocity = 2)
        car2.moveToLane(state.getLine(road = 1), newPosition = 3)
        car2.distanceLeftToMove = 2

        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.resolveIntersections(state)

        // then
        assertThat(car1.currentLane).isEqualTo(destinationLane)
        assertThat(car2.currentLane).isEqualTo(destinationLane)
        assertThat(car1.positionRelativeToStart).isIn(expectedOneOfTheCarsPosition, expectedSecondOfTheCarsPosition)
        assertThat(car2.positionRelativeToStart).isIn(expectedOneOfTheCarsPosition, expectedSecondOfTheCarsPosition)
    }
}
