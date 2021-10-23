package pl.edu.agh.cs.kraksim.simulation.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.agh.cs.kraksim.simulation.application.MapMapper
import pl.edu.agh.cs.kraksim.simulation.application.MapService
import pl.edu.agh.cs.kraksim.simulation.domain.MapDTO

@RequestMapping("/map")
@RestController
class MapController(
    val mapper: MapMapper,
    val service: MapService
) {

    @PostMapping
    fun createMap(@RequestBody mapDTO: MapDTO) {
        service.createMap(mapDTO)
    }

    @GetMapping
    fun getMap(@RequestParam id: Long): ResponseEntity<MapDTO> {
        val convertToDto = mapper.convertToDto(service.getById(id))
        return ResponseEntity.ok(convertToDto)
    }

    @GetMapping("/ids")
    fun getAllIds(): ResponseEntity<List<Long>> {
        val ids: List<Long> = service.getAllIds()
        return ResponseEntity.ok(ids)
    }
}
