package com.example.echo_api.exception.custom.conflict;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user does not
 * already follow the target user.
 */
public class NotFollowingException extends ConflictException {

    /**
     * Constructs a {@code NotFollowingException} with a default message.
     */
    public NotFollowingException() {
        super(ErrorMessageConfig.Conflict.NOT_FOLLOWING);
    }

}
