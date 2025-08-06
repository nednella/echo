package com.example.echo_api.exception.custom.badrequest;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because a user already exists with the
 * specified Clerk ID.
 */
public class ClerkIdAlreadyExistsException extends BadRequestException {

    /**
     * Constructs a {@code ClerkIdAlreadyExistsException} with a default message.
     */
    public ClerkIdAlreadyExistsException() {
        super(ErrorMessageConfig.BadRequest.CLERK_ID_ALREADY_EXISTS);
    }

}
