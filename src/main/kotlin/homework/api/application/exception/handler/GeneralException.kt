package homework.api.application.exception.handler

import org.springframework.http.HttpStatus

class GeneralException(message: String?, cause: Throwable?, httpStatus: HttpStatus) : RuntimeException(message, cause) {
    val httpStatus :HttpStatus = httpStatus
}