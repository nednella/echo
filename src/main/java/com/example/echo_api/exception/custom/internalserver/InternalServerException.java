package com.example.echo_api.exception.custom.internalserver;

import org.springframework.http.HttpStatus;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.ApplicationException;

/**
 * Abstract base class for exceptions representing HTTP 500 Internal Server
 * errors.
 * 
 * <p>
 * Subclasses should provide specific error messages from
 * {@link ErrorMessageConfig.InternalServerError} alongside an optional details
 * field should it be required.
 * 
 * @see ApplicationException
 * @see ErrorMessageConfig.InternalServerError
 */
public abstract class InternalServerException extends ApplicationException {

    /**
     * Constructs a {@code InternalServerException} with a custom message and
     * details.
     *
     * @param message The general error message.
     * @param details The specific error details.
     */
    protected InternalServerException(String message, String details) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, details);
    }

    /**
     * Constructs a {@code InternalServerException} with a custom message.
     *
     * @param message The general error message.
     */
    protected InternalServerException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

}
