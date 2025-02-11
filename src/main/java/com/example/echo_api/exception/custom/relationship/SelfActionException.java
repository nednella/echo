package com.example.echo_api.exception.custom.relationship;

/**
 * Thrown if a HTTP request is rejected because the authenticated user tries to
 * create/delete a relationship with their own profile.
 */
public class SelfActionException extends RelationshipException {

    /**
     * Constructs a {@code InvalidRelationshipException} with the specified message.
     * 
     * @param msg The error message.
     */
    public SelfActionException(String msg) {
        super(msg);
    }

}
