package com.example.echo_api.exception.custom.conflict;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user tries to
 * create/delete a relationship with their own profile.
 */
public class SelfActionException extends ConflictException {

    /**
     * Constructs a {@code SelfActionException} with the a default message.
     */
    public SelfActionException() {
        super(ErrorMessageConfig.Conflict.SELF_ACTION);
    }

}
