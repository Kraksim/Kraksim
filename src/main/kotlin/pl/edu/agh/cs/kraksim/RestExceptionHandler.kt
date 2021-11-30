package pl.edu.agh.cs.kraksim

import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.edu.agh.cs.kraksim.common.exception.InvalidSimulationStateConfigurationException
import pl.edu.agh.cs.kraksim.common.exception.ObjectNotFoundException
import javax.validation.ConstraintViolationException

@ControllerAdvice
class RestExceptionHandler {

    private val log = LogManager.getLogger()

    @ExceptionHandler(value = [ObjectNotFoundException::class])
    protected fun handleNotFound(ex: ObjectNotFoundException): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(value = [InvalidSimulationStateConfigurationException::class])
    protected fun handleInvalidSimulationStateConfigurationException(ex: InvalidSimulationStateConfigurationException): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(value = [IllegalStateException::class])
    protected fun handleIllegalState(ex: IllegalStateException): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleDefault(ex: Exception): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString())
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValid(ex: MethodArgumentNotValidException): ResponseEntity<Any?>? {
        log.error(ex.stackTraceToString())
        val message = ex.bindingResult
            .fieldErrors
            .joinToString(separator = ",\n") {
                "${it.field} - ${it.defaultMessage}, but was \'${it.rejectedValue}\'"
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValid2(ex: ConstraintViolationException): ResponseEntity<Any?>? {
        log.error(ex.stackTraceToString())
        val message = ex.constraintViolations
            .joinToString(separator = "\n") {
                it.propertyPath.toString() + " - " + it.message
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }
}
