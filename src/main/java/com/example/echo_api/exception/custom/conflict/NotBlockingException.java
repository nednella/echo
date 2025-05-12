package com.example.echo_api.exception.custom.conflict;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user does not
 * already block the target user.
 */
public class NotBlockingException extends ConflictException {

    /**
     * Constructs a {@code NotBlockingException} with a default message.
     */
    public NotBlockingException() {
        super(ErrorMessageConfig.Conflict.NOT_BLOCKING);
    }

}
