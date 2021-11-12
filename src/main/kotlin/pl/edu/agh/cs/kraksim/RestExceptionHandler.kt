package pl.edu.agh.cs.kraksim

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.edu.agh.cs.kraksim.common.exception.ObjectNotFoundException

@ControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(value = [ObjectNotFoundException::class])
    protected fun handleNotFound(ex: ObjectNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.toString())
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleDefault(ex: Exception): ResponseEntity<Any> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString())
    }
}
