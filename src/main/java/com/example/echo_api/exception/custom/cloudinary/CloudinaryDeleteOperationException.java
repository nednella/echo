package com.example.echo_api.exception.custom.cloudinary;

import com.cloudinary.Cloudinary;

/**
 * Thrown if there is an error during a delete operation using the
 * {@link Cloudinary} SDK.
 */
public class CloudinaryDeleteOperationException extends CloudinaryException {

    /**
     * Constructs a {@link CloudinaryDeleteOperationException} with the specified
     * {@code message} and {@code cause}.
     * 
     * @param message The detail message.
     * @param cause   The root cause.
     */
    public CloudinaryDeleteOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@link CloudinaryDeleteOperationException} with the specified
     * {@code message}.
     * 
     * @param message The detail message.
     */
    public CloudinaryDeleteOperationException(String message) {
        this(message, null);
    }
}
