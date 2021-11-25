package pl.edu.agh.cs.kraksim.simulation.application

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.common.exception.ObjectNotFoundException
import pl.edu.agh.cs.kraksim.simulation.db.MapRepository
import pl.edu.agh.cs.kraksim.simulation.domain.*
import pl.edu.agh.cs.kraksim.simulation.web.request.CreateMapRequest

@Service
class MapService(
    val mapper: RequestToEntityMapper,
    val mapRepository: MapRepository
) {

    fun createMap(createMapRequest: CreateMapRequest) {
        val map = mapper.createMap(createMapRequest)
        mapRepository.save(map)
    }

    fun getById(id: Long): MapEntity {
        return mapRepository.findByIdOrNull(id) ?: throw ObjectNotFoundException("Couldn't find map with id = $id")
    }

    fun getBasicById(id: Long): BasicMapInfoDTO {
        val map = mapRepository.findByIdOrNull(id) ?: throw ObjectNotFoundException("Couldn't find map with id = $id")
        return getBasicMapInfoDTO(map)
    }

    fun getAllMapsBasicInfo(): List<BasicMapInfoDTO> {
        val all = mapRepository.findAll()
        return all.map { entity -> getBasicMapInfoDTO(entity) }
    }

    private fun getBasicMapInfoDTO(entity: MapEntity) = BasicMapInfoDTO(
        type = entity.type,
        name = entity.name,
        id = entity.id,
        nodes = calculateNodes(entity),
        edges = calculateEdges(entity)
    )

    private fun calculateEdges(entity: MapEntity): List<BasicEdgeDto> {
        val fromMap = HashMap<Long, RoadNodeEntity>()
        val toMap = HashMap<Long, RoadNodeEntity>()

        entity.roadNodes.forEach { node ->
            node.endingRoads.forEach { road ->
                toMap[road.id] = node
            }

            node.startingRoads.forEach { road ->
                fromMap[road.id] = node
            }
        }

        return entity.roads.map { e ->
            BasicEdgeDto(
                from = fromMap[e.id]!!.id,
                to = toMap[e.id]!!.id,
                lanesThickness = e.lanes.size,
            )
        }
    }

    private fun calculateNodes(entity: MapEntity): List<BasicRoadNodeDto> {
        return entity.roadNodes.map {
            BasicRoadNodeDto(
                name = it.name,
                type = it.type,
                id = it.id,
                position = PositionDTO(x = it.position.x, y = it.position.y)
            )
        }
    }
}
