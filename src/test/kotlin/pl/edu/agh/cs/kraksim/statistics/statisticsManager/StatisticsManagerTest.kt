package pl.edu.agh.cs.kraksim.statistics.statisticsManager

import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.*
import pl.edu.agh.cs.kraksim.nagelCore.NagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelCar
import pl.edu.agh.cs.kraksim.statistics.StatisticsManager

internal class StatisticsManagerTest {
    @Test
    fun `Given a state in first turn, with moving car and without expected velocities , create statistics`() {
        // given
        val state = getOneRoadSimulationState(10)
        val initialVelocity = 6
        val car = NagelCar(
            velocity = initialVelocity,
            gps = mockGps()
        )
        car.moveToLane(state.getLane(), 0)
        val manager = StatisticsManager(ArrayList(), emptyMap())
        val roadId = state.getLane().id

        // when
        val result = manager.createStatistics(state)
        println(result)

        // then
        val assertObject = StateStatisticsAssert(result)
        assertObject
            .assertCurrentDensity(roadId, 0.1)
            .assertCurrentFlowRatio(roadId, null)
            .assertCurrentRoadAverageSpeed(roadId, 6.0)
            .assertCurrentWholeMapAverageSpeed(6.0)
            .assertTotalDensity(roadId, 0.1)
            .assertTotalFlowRatio(roadId, null)
            .assertTotalRoadAverageSpeed(roadId, 6.0)
            .assertTotalWholeMapAverageSpeed(6.0)
    }

    @Test
    fun `Given a state in first turn, with multiple moving cars and without expected velocities , create statistics`() {
        // given
        val state = getOneRoadSimulationState(10)
        val initialVelocity1 = 3
        val car1 = NagelCar(
            velocity = initialVelocity1,
            gps = mockGps()
        )
        car1.moveToLane(state.getLane(), 4)
        val initialVelocity2 = 6
        val car2 = NagelCar(
            velocity = initialVelocity2,
            gps = mockGps()
        )
        car2.moveToLane(state.getLane(), 0)
        val manager = StatisticsManager(ArrayList(), emptyMap())
        val roadId = state.getLane().id

        // when
        val result = manager.createStatistics(state)
        println(result)

        // then
        val assertObject = StateStatisticsAssert(result)
        assertObject
            .assertCurrentDensity(roadId, 0.2)
            .assertCurrentFlowRatio(roadId, null)
            .assertCurrentRoadAverageSpeed(roadId, 4.5)
            .assertCurrentWholeMapAverageSpeed(4.5)
            .assertTotalDensity(roadId, 0.2)
            .assertTotalFlowRatio(roadId, null)
            .assertTotalRoadAverageSpeed(roadId, 4.5)
            .assertTotalWholeMapAverageSpeed(4.5)
    }

    @Test
    fun `Given a state in first turn, with multiple moving cars and without expected velocities, with move , create statistics`() {
        // given
        val state = getOneRoadSimulationState(100)
        val initialVelocity1 = 3
        val car1 = NagelCar(
            velocity = initialVelocity1,
            gps = mockGps()
        )
        car1.moveToLane(state.getLane(), 4)
        val initialVelocity2 = 6
        val car2 = NagelCar(
            velocity = initialVelocity2,
            gps = mockGps()
        )
        car2.moveToLane(state.getLane(), 0)
        val manager = StatisticsManager(ArrayList(), emptyMap())
        val roadId = state.getLane().id
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        manager.createStatistics(state)
        strategy.step(state)
        val result = manager.createStatistics(state)

        // then
        val assertObject = StateStatisticsAssert(result)
        assertObject
            .assertCurrentDensity(roadId, 0.02)
            .assertCurrentFlowRatio(roadId, null)
            .assertCurrentRoadAverageSpeed(roadId, 2.5)
            .assertCurrentWholeMapAverageSpeed(2.5)
            .assertTotalDensity(roadId, 0.02)
            .assertTotalFlowRatio(roadId, null)
            .assertTotalRoadAverageSpeed(roadId, 3.5)
            .assertTotalWholeMapAverageSpeed(3.5)
    }

    @Test
    fun `Given a state in first turn, with multiple moving cars on two lanes and without expected velocities, with move , create statistics`() {
        // given
        val state = getTwoRoadConnectedWithIntersectionSimulationState(10, 10)
        val initialVelocity1 = 3
        val car1 = NagelCar(
            velocity = initialVelocity1,
            gps = mockGps()
        )
        car1.moveToLane(state.getLane(0), 0)
        val initialVelocity2 = 6
        val car2 = NagelCar(
            velocity = initialVelocity2,
            gps = mockGps()
        )
        car2.moveToLane(state.getLane(1), 0)
        val manager = StatisticsManager(ArrayList(), emptyMap())
        val roadId1 = 0.toLong()
        val roadId2 = 1.toLong()

        // when
        val result = manager.createStatistics(state)

        // then
        val assertObject = StateStatisticsAssert(result)
        assertObject
            .assertCurrentDensity(roadId1, 0.1)
            .assertCurrentDensity(roadId2, 0.1)
            .assertCurrentFlowRatio(roadId1, null)
            .assertCurrentFlowRatio(roadId2, null)
            .assertCurrentRoadAverageSpeed(roadId1, 3.0)
            .assertCurrentRoadAverageSpeed(roadId2, 6.0)
            .assertCurrentWholeMapAverageSpeed(4.5)
            .assertTotalDensity(roadId1, 0.1)
            .assertTotalDensity(roadId2, 0.1)
            .assertTotalFlowRatio(roadId1, null)
            .assertTotalFlowRatio(roadId2, null)
            .assertTotalRoadAverageSpeed(roadId1, 3.0)
            .assertTotalRoadAverageSpeed(roadId2, 6.0)
            .assertTotalWholeMapAverageSpeed(4.5)
    }
}
