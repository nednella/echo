package com.example.echo_api.exception.custom.relationship;

/**
 * Abstract superclass for all exceptions related to {@link Profile}
 * relationship interactions.
 */
public abstract class RelationshipException extends RuntimeException {

    /**
     * Constructs a {@code SocialContextException} with the specified message and no
     * root cause.
     * 
     * @param msg The detail message.
     */
    protected RelationshipException(String msg) {
        super(msg);
    }

}
