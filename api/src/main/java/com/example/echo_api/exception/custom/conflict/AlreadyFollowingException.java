package com.example.echo_api.exception.custom.conflict;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user already
 * follows the target user.
 */
public class AlreadyFollowingException extends ConflictException {

    /**
     * Constructs a {@code AlreadyFollowingException} with a default message.
     */
    public AlreadyFollowingException() {
        super(ErrorMessageConfig.Conflict.ALREADY_FOLLOWING);
    }

}
