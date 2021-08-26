package pl.edu.agh.cs.kraksim.controller.mappers

import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.controller.dto.*
import pl.edu.agh.cs.kraksim.repository.entities.*

@Mapper(uses = [RoadMapper::class, RoadNodeMapper::class])
interface MapMapper {
    fun convertToDto(mapEntity: MapEntity): MapDTO
    fun convertToEntity(mapDTO: MapDTO): MapEntity

    fun convertPositionToDto(positionEntity: PositionEntity): PositionDTO
    fun convertPositionToEntity(positionDTO: PositionDTO): PositionEntity
}

@Mapper(uses = [RoadMapper::class, MapMapper::class])
interface RoadNodeMapper {
    fun convertRoadNodeToDto(roadNodeEntity: RoadNodeEntity): RoadNodeDTO
    fun convertRoadNodeToEntity(roadNodeDTO: RoadNodeDTO): RoadNodeEntity

    fun convertRoadNodeToDtos(roadNodeEntity: List<RoadNodeEntity>): List<RoadNodeDTO>
    fun convertRoadNodeToEntities(roadNodeDTO: List<RoadNodeDTO>): List<RoadNodeEntity>

    fun convertTurnDirectionToDto(turnDirectionEntity: TurnDirectionEntity): TurnDirectionDTO
    fun convertTurnDirectionToEntity(turnDirectionDTO: TurnDirectionDTO): TurnDirectionEntity

    fun convertTurnDirectionToDtos(turnDirectionEntity: List<TurnDirectionEntity>): List<TurnDirectionDTO>
    fun convertTurnDirectionToEntities(turnDirectionDTO: List<TurnDirectionDTO>): List<TurnDirectionEntity>
}

@Mapper
interface RoadMapper {
    fun convertToDto(roadEntity: RoadEntity): RoadDTO
    fun convertToEntity(roadDTO: RoadDTO): RoadEntity

    fun convertToDtos(roadEntity: List<RoadEntity>): List<RoadDTO>
    fun convertToEntities(roadDTO: List<RoadDTO>): List<RoadEntity>

    fun convertLaneToDto(laneEntity: LaneEntity): LaneDTO
    fun convertLaneToEntity(laneDTO: LaneDTO): LaneEntity

    fun convertLaneToDtos(laneEntity: List<LaneEntity>): List<LaneDTO>
    fun convertLaneToEntities(laneDTO: List<LaneDTO>): List<LaneEntity>
}

