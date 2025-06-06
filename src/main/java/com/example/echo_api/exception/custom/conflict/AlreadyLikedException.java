package com.example.echo_api.exception.custom.conflict;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user already
 * likes the target post.
 */
public class AlreadyLikedException extends ConflictException {

    /**
     * Constructs a {@code AlreadyLikedException} with a default message.
     */
    public AlreadyLikedException() {
        super(ErrorMessageConfig.Conflict.ALREADY_LIKED);
    }

}
