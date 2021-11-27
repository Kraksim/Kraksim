package pl.edu.agh.cs.kraksim.statistics.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.agh.cs.kraksim.statistics.application.StatisticsService
import pl.edu.agh.cs.kraksim.statistics.application.mapper.StatisticsMapper
import pl.edu.agh.cs.kraksim.statistics.domain.StatisticsDTO

@RequestMapping("/statistics")
@RestController
class StatisticsController(
    val service: StatisticsService,
    val mapper: StatisticsMapper
) {

    @GetMapping("/{id}")
    fun getStatistics(
        @PathVariable id: Long
    ): ResponseEntity<StatisticsDTO> {
        val stats = service.findById(id)
        val dto = mapper.convertToDto(stats)
        return ResponseEntity.ok(dto)
    }

    @GetMapping("/simulation/{simulationId}")
    fun getAllStatisticsFromSimulation(
        @PathVariable simulationId: Long
    ): ResponseEntity<List<StatisticsDTO>> {
        val dtos = mapper.convertToDtos(service.getStatisticsFromSimulation(simulationId))
        return ResponseEntity.ok(dtos)
    }

    @GetMapping("/simulation/{simulationId}/turnRange")
    fun getAllStatisticsFromSimulationBetweenTurns(
        @PathVariable simulationId: Long,
        @RequestParam from: Long,
        @RequestParam to: Long
    ): ResponseEntity<List<StatisticsDTO>> {
        val dtos = mapper.convertToDtos(service.getStatisticsFromSimulationBetweenTurns(simulationId, from, to))
        return ResponseEntity.ok(dtos)
    }
}
