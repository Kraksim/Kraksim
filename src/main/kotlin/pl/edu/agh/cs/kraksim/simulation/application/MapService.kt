package pl.edu.agh.cs.kraksim.simulation.application

import org.apache.logging.log4j.LogManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.common.exception.InvalidMapConfigurationException
import pl.edu.agh.cs.kraksim.common.exception.ObjectNotFoundException
import pl.edu.agh.cs.kraksim.simulation.db.MapRepository
import pl.edu.agh.cs.kraksim.simulation.domain.*
import pl.edu.agh.cs.kraksim.simulation.web.request.CreateMapRequest
import pl.edu.agh.cs.kraksim.simulation.web.request.CreateRoadNodeRequest
import java.lang.IllegalArgumentException
import kotlin.random.Random

@Service
class MapService(
    val mapper: RequestToEntityMapper,
    val mapRepository: MapRepository
) {
    private val log = LogManager.getLogger()

    fun createMap(createMapRequest: CreateMapRequest): MapEntity {
        validateCreation(createMapRequest)
        log.info("Creating map name=${createMapRequest.name}")
        val map = mapper.createMap(createMapRequest)
        return mapRepository.save(map)
    }

    fun validateMapToDraw(createMapRequest: CreateMapRequest): BasicMapInfoDTO {
        log.info("Validating map name=${createMapRequest.name}")
        val createMap = mapper.createMap(createMapRequest)
        createMap.id = Random.nextLong()
        createMap.roadNodes.forEach { it.id = Random.nextLong() }
        createMap.roads.forEach { it.id = Random.nextLong() }
        return getBasicMapInfoDTO(createMap)
    }

    fun getById(id: Long): MapEntity {
        log.info("Fetching map id=$id")
        return mapRepository.findByIdOrNull(id) ?: throw ObjectNotFoundException("Couldn't find map with id = $id")
    }

    fun getBasicById(id: Long): BasicMapInfoDTO {
        log.info("Fetching basic map id=$id")
        val map = mapRepository.findByIdOrNull(id) ?: throw ObjectNotFoundException("Couldn't find map with id = $id")
        return getBasicMapInfoDTO(map)
    }

    fun getAllMapsBasicInfo(): List<BasicMapInfoDTO> {
        log.info("Fetching all maps basic info")
        val all = mapRepository.findAll()
        return all.map { entity -> getBasicMapInfoDTO(entity) }
    }

    private fun validateCreation(createMapRequest: CreateMapRequest) {
        val errors = ArrayList<String>()

        if (createMapRequest.roadNodes.isEmpty())
            errors.add("Cannot create map without any road nodes")

        if (createMapRequest.roads.isEmpty())
            errors.add("Cannot create map without any roads")

        if (createMapRequest.roadNodes.none { it.type == RoadNodeType.GATEWAY })
            errors.add("Map has to contain at least one gateway")

        if (errors.isNotEmpty())
            throw InvalidMapConfigurationException(errors)
    }

    private fun getBasicMapInfoDTO(entity: MapEntity) = BasicMapInfoDTO(
        type = entity.type,
        name = entity.name,
        description = entity.description,
        id = entity.id,
        compatibleWith = entity.compatibleWith,
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

        return entity.roads
            .map { e ->
                BasicEdgeDto(
                    from = fromMap[e.id]?.id
                        ?: throw InvalidMapConfigurationException(listOf("Road with name='${e.name}' has no attached start")),
                    to = toMap[e.id]?.id
                        ?: throw InvalidMapConfigurationException(listOf("Road with name='${e.name}' has no attached end")),
                    roadThickness = e.lanes.size,
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
