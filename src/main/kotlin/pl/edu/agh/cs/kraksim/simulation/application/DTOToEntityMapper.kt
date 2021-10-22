package pl.edu.agh.cs.kraksim.simulation.application

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.simulation.domain.*

@Component
class DTOToEntityMapper {

    fun createMap(mapDTO: MapDTO): MapEntity {
        val lanes: Map<Long, LaneEntity> = mapDTO.roads.flatMap { it.lanes }.associate {
            it.id to createLane(it)
        }
        val roads: Map<Long, RoadEntity> = mapDTO.roads.associate {
            it.id to createRoad(it, lanes)
        }
        val roadNodes = mapDTO.roadNodes.map {
            createRoadNode(it, roads, lanes)
        }
        return MapEntity(type = mapDTO.type, roadNodes = roadNodes, roads = roads.values.toList())
    }

    fun createLane(laneDTO: LaneDTO) = LaneEntity(
        startingPoint = laneDTO.startingPoint,
        endingPoint = laneDTO.endingPoint,
        indexFromLeft = laneDTO.indexFromLeft
    )

    fun createRoad(roadDTO: RoadDTO, lanes: Map<Long, LaneEntity>) = RoadEntity(
        roadDTO.length,
        roadDTO.lanes.map { laneDTO ->
            lanes[laneDTO.id]!!
        }
    )

    fun createRoadNode(roadNodeDTO: RoadNodeDTO, roads: Map<Long, RoadEntity>, lanes: Map<Long, LaneEntity>) =
        RoadNodeEntity(
            type = roadNodeDTO.type,
            position = PositionEntity(x = roadNodeDTO.position.x, y = roadNodeDTO.position.y),
            endingRoads = roadNodeDTO.endingRoads.map { roadDTO -> roads[roadDTO.id]!! },
            startingRoads = roadNodeDTO.startingRoads.map { roadDTO -> roads[roadDTO.id]!! },
            turnDirections = roadNodeDTO.turnDirections.map { turnDirectionDTO ->
                TurnDirectionEntity(
                    sourceLane = lanes[turnDirectionDTO.sourceLane.id]!!,
                    destinationRoad = roads[turnDirectionDTO.destinationRoad.id]!!
                )
            }
        )
}
