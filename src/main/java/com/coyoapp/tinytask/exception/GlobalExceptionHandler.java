package com.coyoapp.tinytask.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTaskNotFound(
      TaskNotFoundException ex, WebRequest request) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(Instant.now())
            .message(ex.getMessage())
            .details(request.getDescription(false))
            .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> fieldErrors = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      fieldErrors.put(error.getField(), error.getDefaultMessage());
    }

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(Instant.now())
            .message("Validation failed")
            .details("One or more fields are invalid")
            .fieldErrors(fieldErrors)
            .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .timestamp(Instant.now())
            .message("An unexpected error occurred")
            .details(request.getDescription(false))
            .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
