package com.example.echo_api.exception.custom.file;

import com.example.echo_api.config.ValidationMessageConfig;

/**
 * Thrown if a supplied {@link MultipartFile} is invalid in any way.
 */
public class FileInvalidException extends FileException {

    /**
     * Constructs a {@link FileInvalidException} with a default custom message and
     * the specified {@code cause}.
     * 
     * @param cause The root cause.
     */
    public FileInvalidException(Throwable cause) {
        super(ValidationMessageConfig.INVALID_FILE, cause);
    }

    /**
     * Constructs a {@link FileInvalidException} with a default custom message.
     */
    public FileInvalidException() {
        this(null);
    }

}
