package com.recipe_maker.backend.exceptions;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** A class representing an error response for the GlobalExceptionHandler class. */
@AllArgsConstructor
@Builder
@Data
public class ErrorMessage {
    /** The timestamp of the error response. */
    private final Instant timestamp;

    /** The HTTP status code of the error response. */
    private final int status;

    /** The error description. */
    private final String error;

    /** The error message. */
    private final String message;

    /** The path of the request that caused the error. */
    private final String path;
}
