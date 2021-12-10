package pl.edu.agh.cs.kraksim

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.edu.agh.cs.kraksim.common.exception.ConfigurationErrorService
import pl.edu.agh.cs.kraksim.common.exception.InvalidConfigurationException
import pl.edu.agh.cs.kraksim.common.exception.ObjectNotFoundException
import javax.validation.ConstraintViolationException

@ControllerAdvice
class RestExceptionHandler(
    val errorService: ConfigurationErrorService
) {

    private val log = LogManager.getLogger()

    @ExceptionHandler(value = [ObjectNotFoundException::class])
    protected fun handleNotFound(ex: ObjectNotFoundException): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    protected fun handleBadRequestJson(ex: HttpMessageNotReadableException): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())

        val result = when (ex.cause) {
            is MissingKotlinParameterException -> {
                val parameter = (ex.cause as MissingKotlinParameterException).parameter
                "Invalid parameter '${parameter.name}' configuration."
            }
            else -> ex.cause?.message?.split("\n")?.get(0)
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result)
    }

    @ExceptionHandler(value = [InvalidConfigurationException::class])
    protected fun handleInvalidSimulationStateConfigurationException(ex: InvalidConfigurationException): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(value = [IllegalStateException::class])
    protected fun handleIllegalState(ex: IllegalStateException): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())
        if (errorService.errors.isNotEmpty()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorService.errorMessage)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleDefault(ex: Exception): ResponseEntity<Any> {
        log.error(ex.stackTraceToString())
        if (errorService.errors.isNotEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorService.errorMessage)
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error.")
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
