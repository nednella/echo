package com.example.echo_api.exception.custom.socialcontext;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user already
 * blocks the target user.
 */
public class AlreadyBlockingException extends SocialContextException {

    /**
     * Constructs a {@code AlreadyBlockingException} with the specified message.
     */
    public AlreadyBlockingException() {
        super(ErrorMessageConfig.ALREADY_BLOCKING);
    }

}
