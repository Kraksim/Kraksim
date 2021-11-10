package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.multilaneNagel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.MockRandomProvider
import pl.edu.agh.cs.kraksim.common.getLane
import pl.edu.agh.cs.kraksim.common.testMultiLaneNagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.multilaneNagel.utils.getTwoRoadConnectedWithIntersectionMultiLaneSimulationState
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelCar
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.gps.GPSType

internal class MultiLaneNagelMovementSimulationStrategyTest {

    @Test
    fun `Given car in lane, when full step change lane and go through intersection`() {
        // given
        val state = getTwoRoadConnectedWithIntersectionMultiLaneSimulationState()
        val car = NagelCar(
            velocity = 4,
            gps = GPS(route = listOf(state.roads[0]!!, state.roads[1]!!), type = GPSType.DIJKSTRA_ROAD_LENGTH).apply { popNext() }
        )
        car.moveToLaneFront(state.getLane(roadId = 0, laneId = 11), 0)
        val strategy = testMultiLaneNagelMovementSimulationStrategy(randomProvider = MockRandomProvider(booleanToReturn = false, randomElementIndex = 1))

        // when
        strategy.step(state)

        // then
        assertThat(car.currentLane).isEqualTo(state.getLane(roadId = 1, laneId = 13))
    }
}
