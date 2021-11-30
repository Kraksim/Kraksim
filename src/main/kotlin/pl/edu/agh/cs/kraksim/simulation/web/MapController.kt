package pl.edu.agh.cs.kraksim.simulation.web

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.edu.agh.cs.kraksim.simulation.application.MapMapper
import pl.edu.agh.cs.kraksim.simulation.application.MapService
import pl.edu.agh.cs.kraksim.simulation.domain.MapDTO
import pl.edu.agh.cs.kraksim.simulation.domain.BasicMapInfoDTO
import pl.edu.agh.cs.kraksim.simulation.web.request.CreateMapRequest
import javax.validation.Valid

@Validated
@RequestMapping("/map")
@RestController
class MapController(
    val mapper: MapMapper,
    val service: MapService
) {

    @PostMapping("/create")
    fun createMap(@Valid @RequestBody createMapRequest: CreateMapRequest): ResponseEntity<MapDTO> {
        val result = mapper.convertToDto(service.createMap(createMapRequest))
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getMap(@PathVariable id: Long): ResponseEntity<MapDTO> {
        val result = mapper.convertToDto(service.getById(id))
        return ResponseEntity.ok(result)
    }

    @GetMapping("/basic/{id}")
    fun getBasicMap(@PathVariable id: Long): ResponseEntity<BasicMapInfoDTO> {
        val map = service.getBasicById(id)
        return ResponseEntity.ok(map)
    }

    @GetMapping("/all")
    fun getAllIds(): ResponseEntity<List<BasicMapInfoDTO>> {
        val basicMaps = service.getAllMapsBasicInfo()
        return ResponseEntity.ok(basicMaps)
    }
}
