package pl.edu.agh.cs.kraksim.simulation.application

import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.simulation.db.MapRepository
import pl.edu.agh.cs.kraksim.simulation.domain.MapEntity
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
        return mapRepository.getById(id)
    }

    fun getAllIds(): List<Long> {
        return mapRepository.findAllIds().map { it.id }
    }
}
