package pl.edu.agh.cs.kraksim.statistics.statisticsManager

import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.*
import pl.edu.agh.cs.kraksim.common.gps.MockRoadGps
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
            gps = MockRoadGps()
        )
        car.moveToLane(state.getLane(), 0)
        val manager = StatisticsManager(emptyList(), emptyMap())
        val roadId = RoadId(state.getLane().id)

        // when
        val result = manager.createStatistics(state)

        // then
        val assertObject = StateStatisticsAssert(result)
        assertObject
            .assertCurrentDensity(roadId, Density(0.1))
            .assertCurrentFlowRatio(roadId, null)
            .assertCurrentRoadAverageSpeed(roadId, AverageSpeed(6.0))
            .assertCurrentWholeMapAverageSpeed(AverageSpeed(6.0))
            .assertTotalDensity(roadId, Density(0.1))
            .assertTotalFlowRatio(roadId, null)
            .assertTotalRoadAverageSpeed(roadId, AverageSpeed(6.0))
            .assertTotalWholeMapAverageSpeed(AverageSpeed(6.0))
    }
}
