package com.example.echo_api.exception.custom.badrequest;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the supplied username already
 * exists.
 */
public class UsernameAlreadyExistsException extends BadRequestException {

    /**
     * Constructs a {@code UsernameAlreadyExistsException} with a default message.
     */
    public UsernameAlreadyExistsException() {
        super(ErrorMessageConfig.BadRequest.USERNAME_ARLEADY_EXISTS);
    }

}
