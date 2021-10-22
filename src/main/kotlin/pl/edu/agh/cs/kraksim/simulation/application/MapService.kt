package pl.edu.agh.cs.kraksim.simulation.application

import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.simulation.domain.*

@Service
class MapService(val mapper: DTOToEntityMapper) {

    fun createMap(mapDTO: MapDTO): MapEntity {
        return mapper.createMap(mapDTO)
    }

}
