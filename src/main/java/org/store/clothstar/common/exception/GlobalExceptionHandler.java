package org.store.clothstar.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ErrorResponse illegalArgumentHandler(IllegalArgumentException e) {
        log.error("[IllegalArgument Handler] {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        e.printStackTrace();
        return errorResponse;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private ErrorResponse exHandler(Exception e) {
        log.error("[Exception Handler] {}", e.getMessage());
        e.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return errorResponse;
    }
}