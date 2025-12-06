package com.example.app.dbdesignbackend.util;

import com.example.app.dbdesignbackend.dto.ExceptionDTO;
import com.example.app.dbdesignbackend.dto.ValidationErrorResponse;
import com.example.app.dbdesignbackend.exception.BadRequestException;
import com.example.app.dbdesignbackend.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ExceptionDTO> handleBadRequestException(BadRequestException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage());
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationErrorResponse.Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationErrorResponse.Violation(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new ValidationErrorResponse(
                        "Validation failed",
                        violations
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ExceptionDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDTO);
    }

}
