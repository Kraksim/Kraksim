package pl.edu.agh.cs.kraksim.api

import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.repository.MapRepository

@Service
class Service(
    val repository: MapRepository,
    val stateFactory: StateFactory
) {

    fun main() {
        val simulationEntity = repository.getById(0L)
        val simulationState = stateFactory.from(simulationEntity)
    }
}
