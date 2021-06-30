package pl.edu.agh.cs.kraksim.statistics

import pl.edu.agh.cs.kraksim.common.AverageSpeed
import pl.edu.agh.cs.kraksim.common.Density
import pl.edu.agh.cs.kraksim.common.FlowRatio
import pl.edu.agh.cs.kraksim.common.RoadId
import de.vandermeer.asciitable.AsciiTable

data class StateStatistics(
    val simulationId: Long,
    val turn: Long,

    val currentStatisticsValues: StatisticsValues,
    val totalStatisticsValues: StatisticsValues,
) {
    override fun toString(): String {

        return AsciiTable().apply {
            addRule()
            addRow(null, "Simulation ID: $simulationId", null, "Turn: $turn")
            addRule()
            addRow(null, null, null, "Current Statistic Values")
            insertStatistics(this, currentStatisticsValues)
            addRow(null, null, null, "Total Statistic Values")
            insertStatistics(this, totalStatisticsValues)
        }.render()
    }

    private fun insertStatistics(
        statistics: AsciiTable,
        statisticsValues: StatisticsValues
    ) {
        statistics.apply { addRule()
            addRow("Road ID", "Speed", "Density", "Road Flow")
            addRule()
            addRow(
                "Whole Map",
                "%.2f".format(statisticsValues.speedStatistics.wholeMapAverageSpeed.value),
                "------",
                "------"
            )
            addRule()
            for (RoadId in statisticsValues.density.keys) {
                val speed = statisticsValues.speedStatistics.roadAverageSpeed[RoadId]?.value
                val density = statisticsValues.density[RoadId]?.value
                val flow = statisticsValues.roadFlowRatio[RoadId]?.value
                addRow(
                    RoadId.value,
                    if (speed == null) "N/A" else "%.2f".format(speed),
                    if (density == null) "N/A" else "%.2f".format(density),
                    if (flow == null) "N/A" else "%.2f".format(flow)
                )
                addRule()
            }
        }
    }
}

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
