package homework.api.application.exception.handler

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse

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