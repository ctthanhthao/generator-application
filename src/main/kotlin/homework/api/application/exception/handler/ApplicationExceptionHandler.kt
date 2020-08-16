package homework.api.application.exception.handler

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.xml.ws.Response
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler as ResponseEntityExceptionHandler1

@ControllerAdvice
class ApplicationExceptionHandler
{
    @ExceptionHandler(GeneralException::class)
    fun handleBadRequest(ex : GeneralException, response: HttpServletResponse)// : ResponseEntity<String>
    {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.sendError(ex.httpStatus.value(), ex.message)
    }
}