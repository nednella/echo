package com.example.echo_api.exception.custom.relationship;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user is
 * blocked by the target user.
 */
public class BlockedException extends RelationshipException {

    /**
     * Constructs a {@code BlockedException} with the specified message.
     */
    public BlockedException() {
        super(ErrorMessageConfig.BLOCKED);
    }

}
