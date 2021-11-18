package pl.edu.agh.cs.kraksim

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
    @ExceptionHandler(value = [ObjectNotFoundException::class])
    protected fun handleNotFound(ex: ObjectNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(value = [InvalidSimulationStateConfigurationException::class])
    protected fun handleInvalidSimulationStateConfigurationException(ex: InvalidSimulationStateConfigurationException): ResponseEntity<Any> {
        ex.printStackTrace()
        print(ex.exceptions)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleDefault(ex: Exception): ResponseEntity<Any> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString())
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValid(ex: MethodArgumentNotValidException): ResponseEntity<Any?>? {
        val message = ex.bindingResult
            .fieldErrors
            .joinToString(separator = "\n") {
                it.field + " - " + it.defaultMessage
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValid2(ex: ConstraintViolationException): ResponseEntity<Any?>? {
        val message = ex.constraintViolations
            .joinToString(separator = "\n") {
                it.propertyPath.toString() + " - " + it.message
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }
}
