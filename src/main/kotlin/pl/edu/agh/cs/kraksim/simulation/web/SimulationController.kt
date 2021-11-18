package pl.edu.agh.cs.kraksim.simulation.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.agh.cs.kraksim.simulation.application.SimulationMapper
import pl.edu.agh.cs.kraksim.simulation.application.SimulationService
import pl.edu.agh.cs.kraksim.simulation.domain.BasicSimulationInfoDTO
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationDTO
import pl.edu.agh.cs.kraksim.simulation.web.request.CreateSimulationRequest

@RequestMapping("/simulation")
@RestController
class SimulationController(
    val service: SimulationService,
    val simulationMapper: SimulationMapper,
) {

    @GetMapping("/{id}")
    fun getSimulation(
        @PathVariable id: Long
    ): ResponseEntity<SimulationDTO> {
        val simulation = service.getSimulation(id) ?: return ResponseEntity.notFound().build()
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
                turn = it.simulationStateEntities.maxByOrNull { it.turn }?.turn ?: 0,
                movementSimulationStrategyType = it.movementSimulationStrategy.type
            )
        }
        return ResponseEntity.ok(dtos)
    }

    @PostMapping("/simulate")
    fun simulateStep(
        @RequestParam("id") simulationId: Long,
        @RequestParam("times") times: Int,
    ): ResponseEntity<SimulationDTO> {
        val simulation = service.simulateStep(simulationId, times)
        val dto = simulationMapper.convertToDTO(simulation)
        return ResponseEntity.ok(dto)
    }

    @PostMapping("/create")
    @ResponseBody
    fun createSimulation(
        @RequestBody request: CreateSimulationRequest
    ): ResponseEntity<SimulationDTO> {
        val dto = simulationMapper.convertToDTO(service.createSimulation(request))
        return ResponseEntity.ok(dto)
    }

    @PostMapping("/populate")
    fun populate(): ResponseEntity<SimulationDTO> {
        val dto = simulationMapper.convertToDTO(service.populate())
        return ResponseEntity.ok(dto)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteSimulation(@PathVariable("id") id: Long) {
        service.deleteSimulation(id)
    }
}
