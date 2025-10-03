package com.example.echo_api.exception;

import java.time.Instant;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a standardised error response format for the application.
 * 
 * @param timestamp the timestamp when the error occured (ISO-8601 format)
 * @param status    the HTTP status code integer value associated with the error
 * @param message   a short description for the cause of the error
 * @param path      the request path that triggered the error
 */
public record ErrorResponse(
    @NotNull Instant timestamp,
    @NotNull int status,
    @NotNull String message,
    @NotNull String path
) {

    /**
     * Convenience constructor that automatically generates a timestamp and pulls
     * the status code value.
     * 
     * @param status  the HTTP status code associated with the error
     * @param message a short description for the cause of the error
     * @param path    the request path that triggered the error
     */
    public ErrorResponse(HttpStatus status, String message, String path) {
        this(Instant.now(), status.value(), message, path);
    }

    /**
     * Compares two {@link ErrorResponse} objects by status value and error message.
     * The timestamp and path is ignored.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ErrorResponse))
            return false;

        ErrorResponse that = (ErrorResponse) o;

        return (this.status == that.status &&
            Objects.equals(this.message, that.message));
    }

    /**
     * Hash a {@link ErrorResponse} object by status value and error message. The
     * timestamp and path is ignored.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.status, this.message);
    }

}
