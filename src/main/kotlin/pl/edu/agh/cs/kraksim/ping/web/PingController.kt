package pl.edu.agh.cs.kraksim.ping.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.cs.kraksim.ping.application.PingService
import pl.edu.agh.cs.kraksim.ping.domain.Ping
import javax.servlet.http.HttpServletRequest

@RestController
class PingController(
    val service: PingService
) {

    @GetMapping("/ping")
    fun ping(request: HttpServletRequest): ResponseEntity<Ping> {
        val response = service.getPingResponse(request)
        return ResponseEntity.ok(response)
    }

}