package com.recipe_maker.backend.exceptions;

import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/** A class representing a global exception handler for the application. */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handler for all exceptions that are not handled by specific exception handlers.
     * @param ex the exception that was thrown
     * @param request the web request that resulted in the exception
     * @return ResponseEntity containing an ErrorMessage with details about the error
     */
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

    /**
     * Handler for DataIntegrityViolationException, which is thrown when a database 
     * constraint is violated.
     * @param ex the DataIntegrityViolationException that was thrown
     * @param request the web request that resulted in the exception
     * @return ResponseEntity containing an ErrorMessage with details about the error
     */
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

    /**
     * Handler for DisabledException, which is thrown when a user's account is disabled.
     * @param ex the DisabledException that was thrown
     * @param request the web request that resulted in the exception
     * @return ResponseEntity containing an ErrorMessage with details about the error
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorMessage> handleDisabledExceptions(
            DisabledException ex, WebRequest request) {
        ErrorMessage error = ErrorMessage.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.FORBIDDEN.value())
            .error("Forbidden")
            .message("Your account has been disabled. Please contact support for assistance.")
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Handler for LockedException, which is thrown when a user's account is locked.
     * @param ex the LockedException that was thrown
     * @param request the web request that resulted in the exception
     * @return ResponseEntity containing an ErrorMessage with details about the error
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorMessage> handleLockedExceptions(
            LockedException ex, WebRequest request) {
        ErrorMessage error = ErrorMessage.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.FORBIDDEN.value())
            .error("Forbidden")
            .message("Your account has been locked. Please contact support for assistance.")
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Handler for BadCredentialsException, which is thrown when a user provides invalid credentials.
     * @param ex the BadCredentialsException that was thrown
     * @param request the web request that resulted in the exception
     * @return ResponseEntity containing an ErrorMessage with details about the error
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleBadCredentialsExceptions(
            BadCredentialsException ex, WebRequest request) {
        ErrorMessage error = ErrorMessage.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.FORBIDDEN.value())
            .error("Forbidden")
            .message("Invalid username or password.")
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Handler for IllegalArgumentException, which is thrown when an illegal argument 
     * is passed to a method.
     * 
     * @param ex the IllegalArgumentException that was thrown
     * @param request the web request that resulted in the exception
     * @return ResponseEntity containing an ErrorMessage with details about the error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentExceptions(
            IllegalArgumentException ex, WebRequest request) {
        ErrorMessage error = ErrorMessage.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Bad Request")
            .message(ex.getMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for UsernameNotFoundException, which is thrown when the username is not 
     * found in the database.
     * 
     * @param ex the UsernameNotFoundException that was thrown
     * @param request the web request that resulted in the exception
     * @return ResponseEntity containing an ErrorMessage with details about the error
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUsernameNotFoundExceptions(
            UsernameNotFoundException ex, WebRequest request) {
        ErrorMessage error = ErrorMessage.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Unauthorized")
            .message("User not found")
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
