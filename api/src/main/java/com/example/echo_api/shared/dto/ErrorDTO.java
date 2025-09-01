package com.example.echo_api.shared.dto;

import java.time.Instant;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Represents a standardised error response format for the application.
 * 
 * @param timestamp the timestamp when the error occured (ISO-8601 format)
 * @param status    the HTTP status integer value associated with the error
 * @param message   a short description for the cause of the error
 * @param path      the request path that triggered the error
 */
// @formatter:off
@JsonInclude(Include.NON_NULL)
public record ErrorDTO(
    String timestamp,
    int status,
    String message,
    String path
) {

    public ErrorDTO(
        HttpStatus status,
        String message,
        String path
    ) {
        this(
            Instant.now().toString(),
            status.value(),
            message,
            path
        );
    }

    /**
     * Compares two {@link ErrorDTO} objects by status code, error reason phrase and
     * error message. The timestamp and path is ignored.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o instanceof ErrorDTO))
            return false;

        ErrorDTO that = (ErrorDTO) o;
        
        return (this.status == that.status &&
            Objects.equals(this.message, that.message));
    }

}
// @formatter:on
