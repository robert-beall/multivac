package com.recipe_maker.backend.exceptions;

import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/** A class representing a global exception handler for the application. */
@ControllerAdvice
public class GlobalExceptionHandler {
    // Catch-all handler for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleAllExceptions(
            Exception ex, WebRequest request) {
        ErrorMessage error = ErrorMessage.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Internal Server Error")
            .message("An unexpected error occurred")
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Catch-all handler for unhandled exceptions
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationExceptions(
            DataIntegrityViolationException ex, WebRequest request) {
        ErrorMessage error = ErrorMessage.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.CONFLICT.value())
            .error("Conflict")
            .message(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
