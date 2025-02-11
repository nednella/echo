package com.example.echo_api.exception.custom.relationship;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user does not
 * follow the target user.
 */
public class NotFollowingException extends RelationshipException {

    /**
     * Constructs a {@code NotFollowingException} with the specified message.
     */
    public NotFollowingException() {
        super(ErrorMessageConfig.NOT_FOLLOWING);
    }

}
