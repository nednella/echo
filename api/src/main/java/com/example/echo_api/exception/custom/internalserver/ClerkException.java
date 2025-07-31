package com.example.echo_api.exception.custom.internalserver;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if there is an error during an operation using the {@link Clerk} SDK.
 */
public class ClerkException extends InternalServerException {

    /**
     * Constructs a {@link ClerkException} with a default message and the specified
     * {@code details}.
     * 
     * @param details The specific error details.
     */
    public ClerkException(String details) {
        super(ErrorMessageConfig.InternalServerError.CLERK_SDK_ERROR, details);
    }

}
