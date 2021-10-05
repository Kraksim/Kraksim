package pl.edu.agh.cs.kraksim.api.controller.mappers

import org.mapstruct.Context
import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.api.controller.dto.*
import pl.edu.agh.cs.kraksim.repository.entities.*

@Mapper(uses = [RoadMapper::class, RoadNodeMapper::class])
interface MapMapper {
    fun convertToDto(mapEntity: MapEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): MapDTO
    fun convertToEntity(mapDTO: MapDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): MapEntity

    fun convertPositionToDto(positionEntity: PositionEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): PositionDTO
    fun convertPositionToEntity(positionDTO: PositionDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): PositionEntity
}

@Mapper(uses = [RoadMapper::class, MapMapper::class])
interface RoadNodeMapper {
    fun convertRoadNodeToDto(roadNodeEntity: RoadNodeEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): RoadNodeDTO
    fun convertRoadNodeToEntity(roadNodeDTO: RoadNodeDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): RoadNodeEntity

    fun convertRoadNodeToDtos(
        roadNodeEntity: List<RoadNodeEntity>,
        @Context context: CycleAvoidingMappingContext
    ): List<RoadNodeDTO>

    fun convertRoadNodeToEntities(
        roadNodeDTO: List<RoadNodeDTO>,
        @Context context: CycleAvoidingMappingContext
    ): List<RoadNodeEntity>

    fun convertTurnDirectionToDto(
        turnDirectionEntity: TurnDirectionEntity,
        @Context context: CycleAvoidingMappingContext
    ): TurnDirectionDTO

    fun convertTurnDirectionToEntity(
        turnDirectionDTO: TurnDirectionDTO,
        @Context context: CycleAvoidingMappingContext
    ): TurnDirectionEntity

    fun convertTurnDirectionToDtos(
        turnDirectionEntity: List<TurnDirectionEntity>,
        @Context context: CycleAvoidingMappingContext
    ): List<TurnDirectionDTO>

    fun convertTurnDirectionToEntities(
        turnDirectionDTO: List<TurnDirectionDTO>,
        @Context context: CycleAvoidingMappingContext
    ): List<TurnDirectionEntity>
}

@Mapper
interface RoadMapper {
    fun convertToDto(roadEntity: RoadEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): RoadDTO
    fun convertToEntity(roadDTO: RoadDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): RoadEntity

    fun convertToDtos(roadEntity: List<RoadEntity>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<RoadDTO>
    fun convertToEntities(roadDTO: List<RoadDTO>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<RoadEntity>

    fun convertLaneToDto(laneEntity: LaneEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): LaneDTO
    fun convertLaneToEntity(laneDTO: LaneDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): LaneEntity

    fun convertLaneToDtos(laneEntity: List<LaneEntity>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<LaneDTO>
    fun convertLaneToEntities(laneDTO: List<LaneDTO>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<LaneEntity>
}
