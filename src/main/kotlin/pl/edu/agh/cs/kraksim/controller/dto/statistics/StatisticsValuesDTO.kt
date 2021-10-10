package pl.edu.agh.cs.kraksim.controller.dto.statistics

import pl.edu.agh.cs.kraksim.common.Density
import pl.edu.agh.cs.kraksim.common.FlowRatio
import pl.edu.agh.cs.kraksim.common.RoadId

class StatisticsValuesDTO(
    val speedStatistics: SpeedStatisticsDTO,
    val density: Map<RoadId, Density>,
    val roadFlowRatio: Map<RoadId, FlowRatio>
) {
    var id: Long = 0
}
