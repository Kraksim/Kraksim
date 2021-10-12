package pl.edu.agh.cs.kraksim.statistics.domain

class StatisticsDTO(
    val simulationId: Long,
    val turn: Long,
    val currentStatisticsValues: StatisticsValuesDTO,
    val totalStatisticsValues: StatisticsValuesDTO,
)
