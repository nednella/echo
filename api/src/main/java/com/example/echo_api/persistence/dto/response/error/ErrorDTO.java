package com.example.echo_api.persistence.dto.response.error;

import java.time.Instant;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Represents a standardised error response format for the application.
 * 
 * @param timestamp The timestamp when the error occured (ISO-8601 format).
 * @param status    The HTTP status code associated to the error.
 * @param message   A brief description of the error.
 * @param details   Additional information about the error (optional).
 * @param path      The request path that triggered the error.
 */
// @formatter:off
@JsonInclude(Include.NON_NULL)
public record ErrorDTO(
    String timestamp,
    int status,
    String message,
    String details,
    String path
) {

    public ErrorDTO(
        HttpStatus status,
        String message,
        String details,
        String path
    ) {
        this(
            Instant.now().toString(),
            status.value(),
            message,
            details,
            path
        );
    }

    /**
     * Compares two {@link ErrorDTO} objects by status code, message and
     * details. The timestamp field is ignored.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (this.getClass() != o.getClass())
            return false;

        ErrorDTO that = (ErrorDTO) o;

        return (this.status == that.status &&
            Objects.equals(this.message, that.message) &&
            Objects.equals(this.details, that.details));
    }

}
// @formatter:on
