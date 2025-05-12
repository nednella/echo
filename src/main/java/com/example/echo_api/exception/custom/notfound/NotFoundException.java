package com.example.echo_api.exception.custom.notfound;

import org.springframework.http.HttpStatus;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.ApplicationException;

/**
 * Abstract base class for exceptions representing HTTP 404 Not Found errors.
 * 
 * <p>
 * Subclasses should provide specific error messages from
 * {@link ErrorMessageConfig.NotFound} alongside an optional details field
 * should it be required.
 * 
 * @see ApplicationException
 * @see ErrorMessageConfig.NotFound
 */
public abstract class NotFoundException extends ApplicationException {

    /**
     * Constructs a {@code NotFoundException} with a default custom message and
     * supplied error details.
     *
     * @param details The error details.
     */
    protected NotFoundException(String details) {
        super(HttpStatus.NOT_FOUND, ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND, details);
    }

    /**
     * Constructs a {@code NotFoundException} with a default custom message.
     *
     */
    protected NotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND);
    }

}
