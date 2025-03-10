package com.example.echo_api.exception.custom.file;

import org.springframework.web.multipart.MultipartFile;

/**
 * Abstract superclass for all exceptions related to a {@link MultipartFile}.
 */
public abstract class FileException extends RuntimeException {

    /**
     * Constructs a {@link FileException} with the specified message and root cause.
     * 
     * @param message The detail message.
     * @param cause   The root cause.
     */
    protected FileException(String message, Throwable cause) {
        super(message, cause);
    }

}
