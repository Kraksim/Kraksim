package pl.edu.agh.cs.kraksim.statistics.application.mapper

import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import pl.edu.agh.cs.kraksim.common.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.simulation.domain.RoadEntity
import pl.edu.agh.cs.kraksim.statistics.domain.StatisticsDTO
import pl.edu.agh.cs.kraksim.statistics.domain.StatisticsEntity

@Mapper(uses = [StatisticsValuesMapper::class])
abstract class StatisticsMapper {
    @Mappings(
        value = [
            Mapping(source = "simulationEntity.id", target = "simulationId"),
            Mapping(source = "simulationEntity.mapEntity.roads", target = "roadNamesList"),
            Mapping(target = "roadNames", ignore = true)
        ]
    )
    abstract fun convertToDto(
        statisticsEntity: StatisticsEntity,
        @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()
    ): StatisticsDTO

    @Mappings(
        value = [
            Mapping(source = "simulationEntity.id", target = "simulationId"),
            Mapping(source = "simulationEntity.mapEntity.roads", target = "roadNamesList")
        ]
    )
    abstract fun convertToDtos(
        statisticsEntity: List<StatisticsEntity>,
        @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()
    ): List<StatisticsDTO>

    fun roadsToRoadNames(road: RoadEntity): Pair<RoadId, String> = Pair(road.id, road.name)
}
