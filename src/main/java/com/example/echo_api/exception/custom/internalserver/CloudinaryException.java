package com.example.echo_api.exception.custom.internalserver;

/**
 * Thrown if there is an error during an operation using the {@link Cloudinary}
 * SDK.
 */
public class CloudinaryException extends InternalServerException {

    /**
     * Constructs a {@link CloudinaryException} with the specified {@code message}.
     * 
     * @param message The general error message.
     */
    public CloudinaryException(String message) {
        super(message);
    }

}
