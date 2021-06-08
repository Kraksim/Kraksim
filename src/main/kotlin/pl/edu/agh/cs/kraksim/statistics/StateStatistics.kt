package pl.edu.agh.cs.kraksim.statistics

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
    val wholeMapAverageSpeed: Double,
    val roadAverageSpeed: Map<RoadId, AverageSpeed>
)

data class FlowRatio(val value: Double)
data class Density(val value: Double)
data class AverageSpeed(val value: Double)

data class CarSpeed(val value: Int)
data class RoadId(val value: Long)
data class Velocity(val value: Long)


data class RoadData(
    val id: RoadId,
    val carsNumber: Int,
    val surface: Int
)
