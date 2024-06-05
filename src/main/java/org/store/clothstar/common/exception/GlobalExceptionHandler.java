package org.store.clothstar.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.store.clothstar.common.dto.ErrorResponseDTO;
import org.store.clothstar.common.dto.ValidErrorResponseDTO;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ResponseEntity<ValidErrorResponseDTO> validationExceptionHandler(ValidationException e) throws JsonProcessingException {
        log.error("[ValidationException Handler] {}", e.getMessage());
        e.printStackTrace();

        Map<String, String> errorMap = objectMapper.readValue(e.getMessage(), Map.class);
        ValidErrorResponseDTO validErrorResponseDTO = new ValidErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), errorMap);

        return new ResponseEntity<>(validErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ResponseEntity<ErrorResponseDTO> illegalArgumentHandler(IllegalArgumentException e) {
        log.error("[IllegalArgument Handler] {}", e.getMessage());
        e.printStackTrace();
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponseDTO> exHandler(Exception e) {
        log.error("[Exception Handler] {}", e.getMessage());
        e.printStackTrace();
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}