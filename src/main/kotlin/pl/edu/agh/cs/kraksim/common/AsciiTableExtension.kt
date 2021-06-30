package pl.edu.agh.cs.kraksim.common

import de.vandermeer.asciitable.AsciiTable
import pl.edu.agh.cs.kraksim.statistics.StatisticsValues

fun AsciiTable.insertStatistics(statisticsValues: StatisticsValues)
{
    addRule()
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
        addRow(
            RoadId.value,
            statisticsValues.speedStatistics.roadAverageSpeed[RoadId]?.value?.format(2) ?: "N/A",
            statisticsValues.density[RoadId]?.value?.format(2) ?: "N/A",
            statisticsValues.roadFlowRatio[RoadId]?.value?.format(2) ?: "N/A"

        )
        addRule()
    }
}