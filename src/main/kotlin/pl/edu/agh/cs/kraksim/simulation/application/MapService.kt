package pl.edu.agh.cs.kraksim.simulation.application

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.common.MapId
import pl.edu.agh.cs.kraksim.common.exception.ConfigurationErrorService
import pl.edu.agh.cs.kraksim.common.exception.ErrorWrapper
import pl.edu.agh.cs.kraksim.common.exception.InvalidMapConfigurationException
import pl.edu.agh.cs.kraksim.common.exception.ObjectNotFoundException
import pl.edu.agh.cs.kraksim.simulation.db.MapRepository
import pl.edu.agh.cs.kraksim.simulation.domain.*
import pl.edu.agh.cs.kraksim.simulation.web.request.CreateMapRequest
import kotlin.random.Random

@Service
class MapService(
    val mapper: RequestToEntityMapper,
    val mapRepository: MapRepository,
    val errorService: ConfigurationErrorService
) {
    private val log = LogManager.getLogger()

    @Autowired
    lateinit var simulationService: SimulationService

    fun createMap(createMapRequest: CreateMapRequest): MapEntity {
        log.info("Creating map name=${createMapRequest.name}")
        val map = mapper.createMap(createMapRequest)
        errorService.validate()
        return mapRepository.save(map)
    }

    fun validateMapToDraw(createMapRequest: CreateMapRequest): ErrorWrapper<BasicMapInfoDTO> {
        log.info("Validating map name=${createMapRequest.name}")
        val createMap = mapper.createMap(createMapRequest)
        createMap.id = Random.nextLong()
        createMap.roadNodes.forEach { it.id = Random.nextLong() }
        createMap.roads.forEach { it.id = Random.nextLong() }
        val basicMapInfoDTO = getBasicMapInfoDTO(createMap, 0)
        return errorService.wrap(basicMapInfoDTO)
    }

    fun getById(id: Long): MapEntity {
        log.info("Fetching map id=$id")
        return mapRepository.findByIdOrNull(id) ?: throw ObjectNotFoundException("Couldn't find map with id = $id")
    }

    fun getBasicById(id: Long): BasicMapInfoDTO {
        log.info("Fetching basic map id=$id")
        val map = mapRepository.findByIdOrNull(id) ?: throw ObjectNotFoundException("Couldn't find map with id = $id")
        val simulationsCount: Map<MapId, Long> = simulationService.getSimulationCountWhere(listOf(id))
        return getBasicMapInfoDTO(map, simulationsCount[id] ?: 0)
    }

    fun getAllMapsBasicInfo(): List<BasicMapInfoDTO> {
        log.info("Fetching all maps basic info")
        val all = mapRepository.findAllByOrderById()
        val simulations: Map<MapId, Long> = simulationService.getSimulationCountWhere(all.map { it.id })
        return all.map { entity -> getBasicMapInfoDTO(entity, simulations[entity.id] ?: 0) }
    }

    private fun getBasicMapInfoDTO(entity: MapEntity, simulationsCount: Long) = BasicMapInfoDTO(
        type = entity.type,
        name = entity.name,
        description = entity.description,
        id = entity.id,
        compatibleWith = entity.compatibleWith,
        nodes = calculateNodes(entity),
        edges = calculateEdges(entity),
        simulationsCount = simulationsCount
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
                    roadName = e.name,
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
