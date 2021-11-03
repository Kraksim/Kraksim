package pl.edu.agh.cs.kraksim.simulation.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.agh.cs.kraksim.simulation.application.MapMapper
import pl.edu.agh.cs.kraksim.simulation.application.MapService
import pl.edu.agh.cs.kraksim.simulation.domain.MapDTO
import pl.edu.agh.cs.kraksim.simulation.web.request.CreateMapRequest

@RequestMapping("/map")
@RestController
class MapController(
    val mapper: MapMapper,
    val service: MapService
) {

    @PostMapping("/create")
    fun createMap(@RequestBody createMapRequest: CreateMapRequest) {
        service.createMap(createMapRequest)
    }

    @GetMapping("/{id}")
    fun getMap(@PathVariable id: Long): ResponseEntity<MapDTO> {
        val convertToDto = mapper.convertToDto(service.getById(id))
        return ResponseEntity.ok(convertToDto)
    }

    @GetMapping("/all")
    fun getAllIds(): ResponseEntity<List<Long>> {
        val ids: List<Long> = service.getAllIds()
        return ResponseEntity.ok(ids)
    }
}
