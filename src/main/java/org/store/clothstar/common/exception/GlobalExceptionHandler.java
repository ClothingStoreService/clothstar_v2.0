package org.store.clothstar.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.store.clothstar.common.dto.ErrorResponseDTO;
import org.store.clothstar.common.dto.ValidErrorResponseDTO;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException", ex);
        ex.fillInStackTrace();

        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMap.put(fieldName, message);
        });

        ValidErrorResponseDTO validErrorResponseDTO = new ValidErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), errorMap);

        return new ResponseEntity<>(validErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ResponseEntity<ErrorResponseDTO> illegalArgumentHandler(IllegalArgumentException ex) {
        log.error("[IllegalArgument Handler] {}", ex.getMessage());
        ex.fillInStackTrace();
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponseDTO> exHandler(Exception ex) {
        log.error("[Exception Handler] {}", ex.getMessage());
        ex.fillInStackTrace();
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}