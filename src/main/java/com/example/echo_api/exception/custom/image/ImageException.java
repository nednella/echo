package com.example.echo_api.exception.custom.image;

/**
 * Abstract superclass for all exceptions related to an {@link Image} entity.
 */
public abstract class ImageException extends RuntimeException {

    /**
     * Constructs a {@link ImageException} with the specified message and root
     * cause.
     * 
     * @param message The detail message.
     * @param cause   The root cause.
     */
    protected ImageException(String message, Throwable cause) {
        super(message, cause);
    }

}
