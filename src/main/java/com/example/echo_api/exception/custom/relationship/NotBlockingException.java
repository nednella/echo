package com.example.echo_api.exception.custom.relationship;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user already
 * blocks the target user.
 */
public class NotBlockingException extends RelationshipException {

    /**
     * Constructs a {@code NotBlockingException} with the specified message.
     */
    public NotBlockingException() {
        super(ErrorMessageConfig.NOT_BLOCKING);
    }

}
