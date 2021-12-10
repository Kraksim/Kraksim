package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.multilaneNagel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.getLane
import pl.edu.agh.cs.kraksim.common.testMultiLaneNagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.multilaneNagel.utils.getTwoRoadConnectedWithIntersectionMultiLaneSimulationState
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelCar
import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.gps.GPSType

internal class ChangeLaneMultiLaneNagelMovementSimulationStrategyTest {

    @Test
    fun `Given car in empty target lane, when change lanes stay`() {
        // given
        val state = getTwoRoadConnectedWithIntersectionMultiLaneSimulationState()
        val lane = state.getLane(roadId = 0, laneId = 10)
        val car = createTestCar(state.roads[0], state.roads[1])
            .apply { moveToLaneFront(lane, 0) }
        state.turn = 2
        val strategy = testMultiLaneNagelMovementSimulationStrategy()

        // when
        strategy.changeLanes(state)

        // then
        assertThat(car.currentLane).isEqualTo(lane)
    }

    @Test
    fun `Given car in lane which ends, when change lanes switch position`() {
        // given
        val state = getTwoRoadConnectedWithIntersectionMultiLaneSimulationState()
        val car = createTestCar(state.roads[0], state.roads[1])
            .apply { moveToLaneFront(state.getLane(roadId = 0, laneId = 11), 0) }
        val strategy = testMultiLaneNagelMovementSimulationStrategy()

        // when
        strategy.changeLanes(state)

        // then
        assertThat(car.currentLane).isEqualTo(state.getLane(roadId = 0, laneId = 10))
    }

    @Test
    fun `Given car in lane which ends and state turn even, when change lanes stay`() {
        // given
        val state = getTwoRoadConnectedWithIntersectionMultiLaneSimulationState()
        val lane = state.getLane(roadId = 0, laneId = 11)
        val car = createTestCar(state.roads[0], state.roads[1])
            .apply { moveToLaneFront(lane, 0) }
        state.turn = 2
        val strategy = testMultiLaneNagelMovementSimulationStrategy()

        // when
        strategy.changeLanes(state)

        // then
        assertThat(car.currentLane).isEqualTo(lane)
    }

    @Test
    fun `Given car in lane which car cannot go through intersection to destination, when change lanes switch position`() {
        // given
        val state = getTwoRoadConnectedWithIntersectionMultiLaneSimulationState(firstRoadRightLaneLength = 20)
        val car = createTestCar(state.roads[0], state.roads[1])
            .apply { moveToLaneFront(state.getLane(roadId = 0, laneId = 11), 0) }
        val strategy = testMultiLaneNagelMovementSimulationStrategy()

        // when
        strategy.changeLanes(state)

        // then
        assertThat(car.currentLane).isEqualTo(state.getLane(roadId = 0, laneId = 10))
    }

    @Test
    fun `Given car is parallel to other car in target lane when change lanes stay`() {
        // given
        val state = getTwoRoadConnectedWithIntersectionMultiLaneSimulationState()
        val lane = state.getLane(roadId = 0, laneId = 11)
        val car1 = createTestCar(state.roads[0], state.roads[1])
            .apply { moveToLaneFront(lane, 0) }
        createTestCar(state.roads[0], state.roads[1])
            .apply { moveToLaneFront(state.getLane(roadId = 0, laneId = 10), 0) }
        val strategy = testMultiLaneNagelMovementSimulationStrategy()

        // when
        strategy.changeLanes(state)

        // then
        assertThat(car1.currentLane).isEqualTo(lane)
    }

    private fun createTestCar(vararg route: Road?): NagelCar {
        if (route.any { it == null }) throw IllegalArgumentException("Road must exist")
        return NagelCar(
            velocity = 4,
            gps = GPS(
                route = route.mapNotNull { it },
                type = GPSType.DIJKSTRA_ROAD_LENGTH
            ).apply { popNext() }
        )
    }
}
