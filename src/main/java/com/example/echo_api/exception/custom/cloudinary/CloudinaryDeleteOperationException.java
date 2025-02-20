package com.example.echo_api.exception.custom.cloudinary;

/**
 * 
 */
public class CloudinaryDeleteOperationException extends CloudinaryException {

    /**
     * Constructs a {@link CloudinaryDeleteOperationException} with the specified
     * {@code message} and {@code cause}.
     */
    public CloudinaryDeleteOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@link CloudinaryDeleteOperationException} with the specified
     * {@code message}.
     */
    public CloudinaryDeleteOperationException(String message) {
        this(message, null);
    }
}
