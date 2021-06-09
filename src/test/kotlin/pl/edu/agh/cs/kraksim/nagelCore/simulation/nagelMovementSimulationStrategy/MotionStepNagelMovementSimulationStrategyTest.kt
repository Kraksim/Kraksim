package pl.edu.agh.cs.kraksim.nagelCore.simulation.nagelMovementSimulationStrategy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.MockRandomProvider

import pl.edu.agh.cs.kraksim.nagelCore.NagelCar
import pl.edu.agh.cs.kraksim.nagelCore.NagelGateway
import pl.edu.agh.cs.kraksim.nagelCore.simulation.NagelMovementSimulationStrategy

internal class MotionStepNagelMovementSimulationStrategyTest {

    @Test
    fun `Given car in lane, when motion increase position`() {
        // given
        val initialPosition = 0
        val expectedPosition = 2
        val state = getOneRoadSimulationState()
        val car = NagelCar(velocity = 2)
        car.moveToLane(state.getLine(), newPosition = initialPosition)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.motion(state)

        //then
        assertThat(car.positionRelativeToStart).isEqualTo(expectedPosition)
    }

    @Test
    fun `Given car in lane approaching gateway, when motion increase position and finish at gateway`() {
        // given
        val initialPosition = 2
        val expectedPosition = 0
        val state = getOneRoadSimulationState()
        val endGateway = state.roads[0].end() as NagelGateway
        val car = NagelCar(velocity = 2)
        car.moveToLane(state.getLine(), newPosition = initialPosition)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.motion(state)

        //then
        assertThat(car.positionRelativeToStart).isEqualTo(expectedPosition)
        assertThat(car.currentLane).isNull()
        assertThat(endGateway.finishedCars).contains(car)
    }

    @Test
    fun `Given 2 cars in lane, when motion increase position of both cars`() {
        // given
        val firstCarInitialPosition = 0
        val firstCarExpectedPosition = 2
        val secondCarInitialPosition = 2
        val secondCarExpectedPosition = 4
        val state = getOneRoadSimulationState(roadLength = 36)
        val car1 = NagelCar(velocity = 2)
        val car2 = NagelCar(velocity = 2)
        car2.moveToLane(state.getLine(), newPosition = secondCarInitialPosition)
        car1.moveToLane(state.getLine(), newPosition = firstCarInitialPosition)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.motion(state)

        //then
        assertThat(car1.positionRelativeToStart).isEqualTo(firstCarExpectedPosition)
        assertThat(car2.positionRelativeToStart).isEqualTo(secondCarExpectedPosition)
    }

    @Test
    fun `Given car in front of intersection, when motion don't change position, change distanceLeftToMove to car velocity`() {
        // given
        val initialPosition = 3
        val expectedPosition = 3
        val expectedDistanceLeftToMove = 2
        val state = getTwoRoadConnectedWithIntersectionSimulationState(firstRoadLength = 18)
        val car = NagelCar(velocity = 2)
        car.moveToLane(state.getLine(), newPosition = initialPosition)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.motion(state)

        //then
        assertThat(car.positionRelativeToStart).isEqualTo(expectedPosition)
        assertThat(car.distanceLeftToMove).isEqualTo(expectedDistanceLeftToMove)
    }

    @Test
    fun `Given car in some distance in front of intersection when motion change position and change distanceLeftToMove to velocity minus distance car already traveled`() {
        // given
        val initialPosition = 2
        val expectedPosition = 3
        val expectedDistanceLeftToMove = 1
        val state = getTwoRoadConnectedWithIntersectionSimulationState(firstRoadLength = 18)
        val car = NagelCar(velocity = 2)
        car.moveToLane(state.getLine(), newPosition = initialPosition)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.motion(state)

        //then
        assertThat(car.positionRelativeToStart).isEqualTo(expectedPosition)
        assertThat(car.distanceLeftToMove).isEqualTo(expectedDistanceLeftToMove)
    }
}