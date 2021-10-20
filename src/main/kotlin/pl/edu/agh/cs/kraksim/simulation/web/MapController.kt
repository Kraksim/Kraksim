package pl.edu.agh.cs.kraksim.simulation.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.agh.cs.kraksim.simulation.application.MapMapper
import pl.edu.agh.cs.kraksim.simulation.application.MapService
import pl.edu.agh.cs.kraksim.simulation.db.MapRepository
import pl.edu.agh.cs.kraksim.simulation.domain.*

@RequestMapping("/map")
@RestController
class MapController(
    val mapper: MapMapper,
    val mapRepository: MapRepository,
    val service: MapService
) {

    @PostMapping
    fun createMap(@RequestBody mapDTO: MapDTO) {
        mapRepository.save(service.createMap(mapDTO))
    }

    @GetMapping
    fun getMap(@RequestParam id: Long): ResponseEntity<MapDTO> {
       return ResponseEntity.ok(mapper.convertToDto(mapRepository.getById(id)))
    }

    @GetMapping("/ids")
    fun getAllIds(): ResponseEntity<List<Long>> {
        return ResponseEntity.ok(mapRepository.findAll().map { it.id })
    }
}