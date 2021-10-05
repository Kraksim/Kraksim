package pl.edu.agh.cs.kraksim.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.cs.kraksim.api.controller.dto.SimulationDTO
import pl.edu.agh.cs.kraksim.api.service.StatisticsService

@RequestMapping("/statistics")
@RestController
class StatisticsController(
    val service: StatisticsService,
) {

    @GetMapping("/{id}")
    fun getSimulation(
        @PathVariable id: Long
    ): ResponseEntity<SimulationDTO> {
        TODO("GALAS TU LENIU PIERDOLONY - MAPPERA DLA STATYSTYK NIE ZROBILES")
    }
}
