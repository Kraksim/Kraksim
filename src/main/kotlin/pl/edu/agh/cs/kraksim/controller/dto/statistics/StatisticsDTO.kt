package pl.edu.agh.cs.kraksim.controller.dto.statistics

class StatisticsDTO(
    val simulationId: Long,
    val turn: Long,
    val currentStatisticsValues: StatisticsValuesDTO,
    val totalStatisticsValues: StatisticsValuesDTO,
)
