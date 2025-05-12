package com.example.echo_api.exception.custom.conflict;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user already
 * blocks the target user.
 */
public class AlreadyBlockingException extends ConflictException {

    /**
     * Constructs a {@code AlreadyBlockingException} with a default message.
     */
    public AlreadyBlockingException() {
        super(ErrorMessageConfig.Conflict.ALREADY_BLOCKING);
    }

}
