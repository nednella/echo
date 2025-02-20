package com.example.echo_api.exception.custom.cloudinary;

/**
 * 
 */
public class CloudinaryUploadOperationException extends CloudinaryException {

    /**
     * Constructs a {@link CloudinaryUploadOperationException} with the specified
     * {@code message} and {@code cause}.
     */
    public CloudinaryUploadOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@link CloudinaryUploadOperationException} with the specified
     * {@code message}.
     */
    public CloudinaryUploadOperationException(String message) {
        this(message, null);
    }

}
