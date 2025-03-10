package com.example.echo_api.exception.custom.cloudinary;

import com.cloudinary.Cloudinary;

/**
 * Thrown if there is an error during an upload operation using the
 * {@link Cloudinary} SDK.
 */
public class CloudinaryUploadOperationException extends CloudinaryException {

    /**
     * Constructs a {@link CloudinaryUploadOperationException} with the specified
     * {@code message} and {@code cause}.
     * 
     * @param message The detail message.
     * @param cause   The root cause.
     */
    public CloudinaryUploadOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@link CloudinaryUploadOperationException} with the specified
     * {@code message}.
     * 
     * @param message The detail message.
     */
    public CloudinaryUploadOperationException(String message) {
        this(message, null);
    }

}
