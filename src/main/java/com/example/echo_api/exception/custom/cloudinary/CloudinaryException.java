package com.example.echo_api.exception.custom.cloudinary;

/**
 * Abstract superclass for all exceptions related to the {@link Cloudinary} SDK.
 */
public abstract class CloudinaryException extends RuntimeException {

    /**
     * Constructs a {@code CloudinaryException} with the specified message and no
     * root cause.
     * 
     * @param message The detail message.
     */
    protected CloudinaryException(String message, Throwable cause) {
        super(message, cause);
    }

}
