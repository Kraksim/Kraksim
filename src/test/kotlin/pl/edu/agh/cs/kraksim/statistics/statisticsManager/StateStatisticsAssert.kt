package pl.edu.agh.cs.kraksim.statistics.statisticsManager

import org.assertj.core.api.AssertionsForClassTypes
import pl.edu.agh.cs.kraksim.common.AverageSpeed
import pl.edu.agh.cs.kraksim.common.Density
import pl.edu.agh.cs.kraksim.common.FlowRatio
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.statistics.StateStatistics

class StateStatisticsAssert(private val stateStatistics: StateStatistics) {
    fun assertCurrentDensity(roadId: RoadId, density: Density): StateStatisticsAssert {
        val currentDensity = stateStatistics.currentStatisticsValues.density[roadId]
        AssertionsForClassTypes.assertThat(currentDensity).isEqualTo(density)
        return this
    }

    fun assertCurrentFlowRatio(roadId: RoadId, flowRatio: FlowRatio?): StateStatisticsAssert {
        val roadFlowRatio = stateStatistics.currentStatisticsValues.roadFlowRatio[roadId]
        AssertionsForClassTypes.assertThat(roadFlowRatio).isEqualTo(flowRatio)
        return this
    }

    fun assertCurrentRoadAverageSpeed(roadId: RoadId, averageSpeed: AverageSpeed?): StateStatisticsAssert {
        val roadAverageSpeed = stateStatistics.currentStatisticsValues.speedStatistics.roadAverageSpeed[roadId]
        AssertionsForClassTypes.assertThat(roadAverageSpeed).isEqualTo(averageSpeed)
        return this
    }

    fun assertCurrentWholeMapAverageSpeed(averageSpeed: AverageSpeed?): StateStatisticsAssert {
        val wholeMapAverageSpeed = stateStatistics.currentStatisticsValues.speedStatistics.wholeMapAverageSpeed
        AssertionsForClassTypes.assertThat(wholeMapAverageSpeed).isEqualTo(averageSpeed)
        return this
    }

    fun assertTotalDensity(roadId: RoadId, density: Density): StateStatisticsAssert {
        val currentDensity = stateStatistics.totalStatisticsValues.density[roadId]
        AssertionsForClassTypes.assertThat(currentDensity).isEqualTo(density)
        return this
    }


    fun assertTotalFlowRatio(roadId: RoadId, flowRatio: FlowRatio?): StateStatisticsAssert {
        val roadFlowRatio = stateStatistics.totalStatisticsValues.roadFlowRatio[roadId]
        AssertionsForClassTypes.assertThat(roadFlowRatio).isEqualTo(flowRatio)
        return this
    }

    fun assertTotalRoadAverageSpeed(roadId: RoadId, averageSpeed: AverageSpeed?): StateStatisticsAssert {
        val roadAverageSpeed = stateStatistics.totalStatisticsValues.speedStatistics.roadAverageSpeed[roadId]
        AssertionsForClassTypes.assertThat(roadAverageSpeed).isEqualTo(averageSpeed)
        return this
    }

    fun assertTotalWholeMapAverageSpeed(averageSpeed: AverageSpeed?): StateStatisticsAssert {
        val wholeMapAverageSpeed = stateStatistics.totalStatisticsValues.speedStatistics.wholeMapAverageSpeed
        AssertionsForClassTypes.assertThat(wholeMapAverageSpeed).isEqualTo(averageSpeed)
        return this
    }
}
