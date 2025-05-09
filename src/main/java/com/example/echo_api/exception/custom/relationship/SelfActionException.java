package com.example.echo_api.exception.custom.relationship;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user tries to
 * create/delete a relationship with their own profile.
 */
public class SelfActionException extends RelationshipException {

    /**
     * Constructs a {@code SelfActionException} with the specified message.
     */
    public SelfActionException() {
        super(ErrorMessageConfig.SELF_ACTION);
    }

}
