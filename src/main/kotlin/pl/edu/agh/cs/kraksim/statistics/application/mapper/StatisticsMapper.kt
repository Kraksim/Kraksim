package pl.edu.agh.cs.kraksim.statistics.application.mapper

import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import pl.edu.agh.cs.kraksim.common.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.statistics.domain.StatisticsDTO
import pl.edu.agh.cs.kraksim.statistics.domain.StatisticsEntity

@Mapper(uses = [StatisticsValuesMapper::class])
interface StatisticsMapper {
    @Mapping(source = "simulationEntity.id", target = "simulationId")
    fun convertToDto(statisticsEntity: StatisticsEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): StatisticsDTO

    @Mapping(source = "simulationEntity.id", target = "simulationId")
    fun convertToDtos(statisticsEntity: List<StatisticsEntity>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<StatisticsDTO>
}
