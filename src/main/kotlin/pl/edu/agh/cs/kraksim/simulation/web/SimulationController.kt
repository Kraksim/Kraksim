package pl.edu.agh.cs.kraksim.simulation.web

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.edu.agh.cs.kraksim.simulation.application.SimulationMapper
import pl.edu.agh.cs.kraksim.simulation.application.SimulationService
import pl.edu.agh.cs.kraksim.simulation.domain.BasicSimulationInfoDTO
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationDTO
import pl.edu.agh.cs.kraksim.simulation.web.request.CreateSimulationRequest
import javax.validation.Valid
import javax.validation.constraints.Positive

@RequestMapping("/simulation")
@RestController
@Validated
class SimulationController(
    val service: SimulationService,
    val simulationMapper: SimulationMapper,
) {

    @GetMapping("/{id}")
    fun getSimulation(
        @PathVariable id: Long
    ): ResponseEntity<SimulationDTO> {
        val simulation = service.getSimulation(id)
        val dto = simulationMapper.convertToDTO(simulation)
        return ResponseEntity.ok(dto)
    }

    @GetMapping("/all")
    fun getSimulations(): ResponseEntity<List<BasicSimulationInfoDTO>> {
        val simulations = service.getAllSimulationsInfo()
        val dtos = simulations.map {
            BasicSimulationInfoDTO(
                id = it.id,
                name = it.name,
                type = it.simulationType,
                mapId = it.mapEntity.id,
                isFinished = it.finished,
                turn = it.simulationStateEntities.map { it.turn }.maxOrNull() ?: 0,
                movementSimulationStrategyType = it.movementSimulationStrategy.type
            )
        }
        return ResponseEntity.ok(dtos)
    }

    @PostMapping("/simulate")
    fun simulateStep(
        @RequestParam("id") simulationId: Long,
        @RequestParam("times") @Valid @Positive times: Int,
    ): ResponseEntity<Void> {
        service.simulateStep(simulationId, times)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/create")
    @ResponseBody
    fun createSimulation(
        @RequestBody @Valid request: CreateSimulationRequest
    ): ResponseEntity<SimulationDTO> {
        val dto = simulationMapper.convertToDTO(service.createSimulation(request))
        return ResponseEntity.ok(dto)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteSimulation(@PathVariable("id") id: Long) {
        service.deleteSimulation(id)
    }
}
