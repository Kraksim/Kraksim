package pl.edu.agh.cs.kraksim.ping.application

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.ping.domain.Ping
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest


@Service
class PingService(
    @Value("\${app.version}")
    val version: String,
    @Value("\${app.name}")
    val name: String
) {

    fun getPingResponse(request: HttpServletRequest): Ping {
        return Ping(
            name = name,
            version = version,
            time = LocalDateTime.now(),
            senderIpAddress = request.getHeader("X-FORWARDED-FOR").ifBlank { request.remoteAddr }
        )
    }

}