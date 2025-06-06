package com.example.echo_api.exception.custom.forbidden;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the authenticated user does not
 * own the resource in question.
 */
public class ResourceOwnershipException extends ForbiddenException {

    /**
     * Constructs a {@code ResourceOwnershipException} with a default message.
     */
    public ResourceOwnershipException() {
        super(ErrorMessageConfig.Forbidden.RESOURCE_OWNERSHIP_REQUIRED);
    }

}
