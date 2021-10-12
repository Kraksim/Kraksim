package pl.edu.agh.cs.kraksim.statistics.domain

import pl.edu.agh.cs.kraksim.common.AverageSpeed
import pl.edu.agh.cs.kraksim.common.RoadId

class SpeedStatisticsDTO(
    val wholeMapAverageSpeed: AverageSpeed,
    val roadAverageSpeed: Map<RoadId, AverageSpeed>
)
