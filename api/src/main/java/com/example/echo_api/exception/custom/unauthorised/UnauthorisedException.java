package com.example.echo_api.exception.custom.unauthorised;

import org.springframework.http.HttpStatus;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.ApplicationException;

/**
 * Abstract base class for exceptions representing HTTP 401 Unauthorized errors.
 * 
 * <p>
 * Subclasses should provide specific error messages from
 * {@link ErrorMessageConfig.Unauthorised} alongside an optional details field
 * should it be required.
 * 
 * @see ApplicationException
 * @see ErrorMessageConfig.Unauthorised
 */
public abstract class UnauthorisedException extends ApplicationException {

    /**
     * Constructs a {@code UnauthorisedException} with a custom message and details.
     *
     * @param message The general error message.
     * @param details The specific error details.
     */
    protected UnauthorisedException(String message, String details) {
        super(HttpStatus.UNAUTHORIZED, message, details);
    }

    /**
     * Constructs a {@code UnauthorisedException} with a custom message.
     *
     * @param message The general error message.
     */
    protected UnauthorisedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

}
