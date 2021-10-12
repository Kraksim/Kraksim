package pl.edu.agh.cs.kraksim.controller.exceptionHandler

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import pl.edu.agh.cs.kraksim.api.exception.ObjectNotFoundException

@ControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(value = [ObjectNotFoundException::class])
    protected fun handleNotFound(ex: RuntimeException, request: WebRequest?): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body<Any>(ex.toString())
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleDefault(ex: RuntimeException, request: WebRequest?): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body<Any>(ex.toString())
    }
}
