package pl.edu.agh.cs.kraksim.statistics

import pl.edu.agh.cs.kraksim.common.AverageSpeed
import pl.edu.agh.cs.kraksim.common.Density
import pl.edu.agh.cs.kraksim.common.FlowRatio
import pl.edu.agh.cs.kraksim.common.RoadId

data class StateStatistics(
    val simulationId: Long,
    val turn: Long,

    val currentStatisticsValues: StatisticsValues,
    val totalStatisticsValues: StatisticsValues,
)

data class StatisticsValues(
    val speedStatistics: SpeedStatistics,
    val density: Map<RoadId, Density>,
    val roadFlowRatio: Map<RoadId, FlowRatio>
)

data class SpeedStatistics(
    val wholeMapAverageSpeed: AverageSpeed,
    val roadAverageSpeed: Map<RoadId, AverageSpeed>
)

data class RoadData(
    val id: RoadId,
    val carsNumber: Int,
    val surface: Int
)
