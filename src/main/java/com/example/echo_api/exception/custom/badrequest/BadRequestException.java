package com.example.echo_api.exception.custom.badrequest;

import org.springframework.http.HttpStatus;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.exception.custom.ApplicationException;

/**
 * Abstract base class for exceptions representing HTTP 400 Bad Request errors.
 * 
 * <p>
 * Subclasses should provide specific error messages from
 * {@link ErrorMessageConfig.BadRequest} alongside an optional details field,
 * such as those from {@link ValidationMessageConfig}.
 * 
 * @see ApplicationException
 * @see ErrorMessageConfig.BadRequest
 * @see ValidationMessageConfig
 */
public abstract class BadRequestException extends ApplicationException {

    /**
     * Constructs a {@code BadRequestException} with a custom message and details.
     *
     * @param message The general error message.
     * @param details The specific error details.
     */
    protected BadRequestException(String message, String details) {
        super(HttpStatus.BAD_REQUEST, message, details);
    }

    /**
     * Constructs a {@code BadRequestException} with a custom message.
     *
     * @param message The general error message.
     */
    protected BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
