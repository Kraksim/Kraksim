package pl.edu.agh.cs.kraksim.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.agh.cs.kraksim.api.exception.MapNotFoundException
import pl.edu.agh.cs.kraksim.api.SimulationService
import pl.edu.agh.cs.kraksim.controller.dto.SimulationDTO
import pl.edu.agh.cs.kraksim.controller.dto.basicInfo.BasicSimulationInfoDTO
import pl.edu.agh.cs.kraksim.controller.mappers.SimulationMapper
import pl.edu.agh.cs.kraksim.controller.requestBody.CreateSimulationRequest

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
        val simulations = service.getAllSimulations()
        val dtos = simulations.map {
            BasicSimulationInfoDTO(
                id = it.id,
                name = it.name,
                type = it.simulationType,
                mapId = it.mapEntity.id
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

    @PutMapping("/create")
    @ResponseBody
    fun createSimulation(
        @RequestBody request: CreateSimulationRequest
    ): ResponseEntity<SimulationDTO> {

        val response = try {
            val dto = simulationMapper.convertToDTO(service.createSimulation(request))
            ResponseEntity.ok(dto)
        } catch (mapException: MapNotFoundException) {
            ResponseEntity.badRequest().build()
        }

        return response
    }

    @GetMapping("/populate")
    fun populate(): ResponseEntity<SimulationDTO> {
        val dto = simulationMapper.convertToDTO(service.populate())
        return ResponseEntity.ok(dto)
    }

    @DeleteMapping("/delete")
    fun deleteSimulation(@RequestParam("simulationId") id: Long) {
        service.deleteSimulation(id)
    }
}
