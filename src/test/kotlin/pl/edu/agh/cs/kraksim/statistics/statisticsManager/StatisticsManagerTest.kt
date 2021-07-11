package pl.edu.agh.cs.kraksim.statistics.statisticsManager

import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.getLane
import pl.edu.agh.cs.kraksim.common.getOneRoadSimulationState
import pl.edu.agh.cs.kraksim.gps.GPS
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
            gps = GPS()
        )
        car.moveToLane(state.getLane(), 0)
        val manager = StatisticsManager(emptyList(), emptyMap())
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
}
