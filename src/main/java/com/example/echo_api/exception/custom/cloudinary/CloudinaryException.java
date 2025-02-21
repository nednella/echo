package com.example.echo_api.exception.custom.cloudinary;

/**
 * Abstract superclass for all exceptions related to the {@link Cloudinary} SDK.
 */
public abstract class CloudinaryException extends RuntimeException {

    /**
     * Constructs a {@link CloudinaryException} with the specified message and root
     * cause.
     * 
     * @param message The detail message.
     * @param cause   The root cause.
     */
    protected CloudinaryException(String message, Throwable cause) {
        super(message, cause);
    }

}
