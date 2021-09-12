package pl.edu.agh.cs.kraksim.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.cs.kraksim.api.SimulationService
import pl.edu.agh.cs.kraksim.controller.dto.SimulationDTO
import pl.edu.agh.cs.kraksim.controller.mappers.SimulationMapper

@RequestMapping("/simulation")
@RestController
class Controller(
    val service: SimulationService,
    val simulationMapper: SimulationMapper
) {

    @GetMapping("/{id}")
    fun getSimulation(
        @PathVariable id: Long
    ): ResponseEntity<SimulationDTO> {

        TODO()
    }
}
