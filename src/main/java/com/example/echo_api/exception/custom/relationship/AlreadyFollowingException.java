package com.example.echo_api.exception.custom.relationship;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user already
 * follows the target user.
 */
public class AlreadyFollowingException extends RelationshipException {

    /**
     * Constructs a {@code AlreadyFollowingException} with the specified message.
     */
    public AlreadyFollowingException() {
        super(ErrorMessageConfig.ALREADY_FOLLOWING);
    }

}
