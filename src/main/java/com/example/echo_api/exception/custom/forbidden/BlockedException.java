package com.example.echo_api.exception.custom.forbidden;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user has a
 * user restriction placed upon them.
 */
public class BlockedException extends ForbiddenException {

    /**
     * Constructs a {@code BlockedException} with the specified message.
     */
    public BlockedException() {
        super(ErrorMessageConfig.Forbidden.BLOCKED);
    }

}
