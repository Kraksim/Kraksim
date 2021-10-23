package pl.edu.agh.cs.kraksim.simulation.application

import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.simulation.db.MapRepository
import pl.edu.agh.cs.kraksim.simulation.domain.MapDTO
import pl.edu.agh.cs.kraksim.simulation.domain.MapEntity

@Service
class MapService(
    val mapper: DTOToEntityMapper,
    val mapRepository: MapRepository
) {

    fun createMap(mapDTO: MapDTO) {
        val map = mapper.createMap(mapDTO)
        mapRepository.save(map)
    }

    fun getById(id: Long): MapEntity {
        return mapRepository.getById(id)
    }

    fun getAllIds(): List<Long> {
        return mapRepository.findAllIds().map { it.id }
    }
}
