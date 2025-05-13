package com.example.echo_api.exception.custom.conflict;

import org.springframework.http.HttpStatus;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.ApplicationException;

/**
 * Abstract base class for exceptions representing HTTP 409 Conflict errors.
 * 
 * <p>
 * Subclasses should provide specific error messages from
 * {@link ErrorMessageConfig.Conflict} alongside an optional details field
 * should it be required.
 * 
 * @see ApplicationException
 * @see ErrorMessageConfig.Conflict
 */
public abstract class ConflictException extends ApplicationException {

    /**
     * Constructs a {@code ConflictException} with a custom message and details.
     *
     * @param message The general error message.
     * @param details The specific error details.
     */
    protected ConflictException(String message, String details) {
        super(HttpStatus.CONFLICT, message, details);
    }

    /**
     * Constructs a {@code ConflictException} with a custom message.
     *
     * @param message The general error message.
     */
    protected ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

}
