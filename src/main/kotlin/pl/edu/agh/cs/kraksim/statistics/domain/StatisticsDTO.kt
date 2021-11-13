package pl.edu.agh.cs.kraksim.statistics.domain

import pl.edu.agh.cs.kraksim.common.AverageSpeed
import pl.edu.agh.cs.kraksim.common.Density
import pl.edu.agh.cs.kraksim.common.FlowRatio
import pl.edu.agh.cs.kraksim.common.RoadId

class StatisticsDTO(
    roadNamesList: List<Pair<RoadId, String>>,
    val simulationId: Long,
    val turn: Long,
    val currentStatisticsValues: StatisticsValuesDTO,
    val totalStatisticsValues: StatisticsValuesDTO,
) {
    val roadNames: Map<RoadId, String> = roadNamesList.toMap()
}

class StatisticsValuesDTO(
    val speedStatistics: SpeedStatisticsDTO,
    val density: Map<RoadId, Density>,
    val roadFlowRatio: Map<RoadId, FlowRatio>
) {
    var id: Long = 0
}

class SpeedStatisticsDTO(
    val wholeMapAverageSpeed: AverageSpeed,
    val roadAverageSpeed: Map<RoadId, AverageSpeed>
)
