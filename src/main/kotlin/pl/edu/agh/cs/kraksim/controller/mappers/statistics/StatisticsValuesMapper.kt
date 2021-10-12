package pl.edu.agh.cs.kraksim.controller.mappers.statistics

import org.mapstruct.Context
import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.controller.dto.statistics.StatisticsValuesDTO
import pl.edu.agh.cs.kraksim.controller.mappers.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.repository.entities.statistics.StatisticsValuesEntity

@Mapper(uses = [SpeedStatisticsMapper::class])
interface StatisticsValuesMapper {
    fun convertToDto(statisticsValuesEntity: StatisticsValuesEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): StatisticsValuesDTO
    fun convertToEntity(statisticsValuesDTO: StatisticsValuesDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): StatisticsValuesEntity
}
