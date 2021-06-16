package pl.edu.agh.cs.kraksim.statistics.statisticsManager

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.AverageSpeed
import pl.edu.agh.cs.kraksim.common.Density
import pl.edu.agh.cs.kraksim.common.FlowRatio
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.nagelCore.nagelMovementSimulationStrategy.getLine
import pl.edu.agh.cs.kraksim.nagelCore.nagelMovementSimulationStrategy.getOneRoadSimulationState
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelCar
import pl.edu.agh.cs.kraksim.statistics.*

internal class StatisticsManagerTest {
    @Test
    fun `Given a state in first turn, with moving car and without expected velocities , create statistics`() {
        //given
        val state = getOneRoadSimulationState(10);
        val initialVelocity = 6;
        val car = NagelCar(
                velocity = initialVelocity,
        )
        car.moveToLane(state.getLine(), 0);
        val manager = StatisticsManager(emptyList(), emptyMap())
        val roadId = RoadId(state.getLine().id)

        //when
        val result = manager.createStatistics(state)

        //then
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

    class StateStatisticsAssert(private val stateStatistics: StateStatistics){
        fun assertCurrentDensity(roadId: RoadId, density: Density): StateStatisticsAssert{
            val currentDensity = stateStatistics.currentStatisticsValues.density[roadId]
            assertThat(currentDensity).isEqualTo(density)
            return this
        }

        fun assertCurrentFlowRatio(roadId: RoadId, flowRatio: FlowRatio?): StateStatisticsAssert{
            val roadFlowRatio = stateStatistics.currentStatisticsValues.roadFlowRatio[roadId]
            assertThat(roadFlowRatio).isEqualTo(flowRatio)
            return this;
        }

        fun assertCurrentRoadAverageSpeed(roadId: RoadId, averageSpeed: AverageSpeed?): StateStatisticsAssert{
            val roadAverageSpeed = stateStatistics.currentStatisticsValues.speedStatistics.roadAverageSpeed[roadId]
            assertThat(roadAverageSpeed).isEqualTo(averageSpeed)
            return this;
        }

        fun assertCurrentWholeMapAverageSpeed(averageSpeed: AverageSpeed?): StateStatisticsAssert{
            val wholeMapAverageSpeed = stateStatistics.currentStatisticsValues.speedStatistics.wholeMapAverageSpeed
            assertThat(wholeMapAverageSpeed).isEqualTo(averageSpeed)
            return this;
        }

        fun assertTotalDensity(roadId: RoadId, density: Density): StateStatisticsAssert{
            val currentDensity = stateStatistics.totalStatisticsValues.density[roadId]
            assertThat(currentDensity).isEqualTo(density)
            return this
        }


        fun assertTotalFlowRatio(roadId: RoadId, flowRatio: FlowRatio?): StateStatisticsAssert{
            val roadFlowRatio = stateStatistics.totalStatisticsValues.roadFlowRatio[roadId]
            assertThat(roadFlowRatio).isEqualTo(flowRatio)
            return this;
        }

        fun assertTotalRoadAverageSpeed(roadId: RoadId, averageSpeed: AverageSpeed?): StateStatisticsAssert{
            val roadAverageSpeed = stateStatistics.totalStatisticsValues.speedStatistics.roadAverageSpeed[roadId]
            assertThat(roadAverageSpeed).isEqualTo(averageSpeed)
            return this;
        }

        fun assertTotalWholeMapAverageSpeed(averageSpeed: AverageSpeed?): StateStatisticsAssert{
            val wholeMapAverageSpeed = stateStatistics.totalStatisticsValues.speedStatistics.wholeMapAverageSpeed
            assertThat(wholeMapAverageSpeed).isEqualTo(averageSpeed)
            return this;
        }
    }

}