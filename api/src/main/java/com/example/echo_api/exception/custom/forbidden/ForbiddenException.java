package com.example.echo_api.exception.custom.forbidden;

import org.springframework.http.HttpStatus;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.ApplicationException;

/**
 * Abstract base class for exceptions representing HTTP 403 Forbidden errors.
 * 
 * <p>
 * Subclasses should provide specific error messages from
 * {@link ErrorMessageConfig.Forbidden} alongside an optional details field
 * should it be required.
 * 
 * @see ApplicationException
 * @see ErrorMessageConfig.Forbidden
 */
public abstract class ForbiddenException extends ApplicationException {

    /**
     * Constructs a {@code ForbiddenException} with a custom message and details.
     *
     * @param message The general error message.
     * @param details The specific error details.
     */
    protected ForbiddenException(String message, String details) {
        super(HttpStatus.FORBIDDEN, message, details);
    }

    /**
     * Constructs a {@code ForbiddenException} with a custom message.
     *
     * @param message The general error message.
     */
    protected ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

}
