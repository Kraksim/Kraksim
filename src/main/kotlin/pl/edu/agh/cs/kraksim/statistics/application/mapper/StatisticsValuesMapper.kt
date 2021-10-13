package pl.edu.agh.cs.kraksim.statistics.application.mapper

import org.mapstruct.Context
import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.common.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.statistics.domain.StatisticsValuesDTO
import pl.edu.agh.cs.kraksim.statistics.domain.StatisticsValuesEntity

@Mapper(uses = [SpeedStatisticsMapper::class])
interface StatisticsValuesMapper {
    fun convertToDto(statisticsValuesEntity: StatisticsValuesEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): StatisticsValuesDTO
    fun convertToEntity(statisticsValuesDTO: StatisticsValuesDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): StatisticsValuesEntity
}
