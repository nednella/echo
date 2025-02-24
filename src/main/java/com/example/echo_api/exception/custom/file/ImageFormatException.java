package com.example.echo_api.exception.custom.file;

import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.config.ValidationMessageConfig;

/**
 * Thrown if a supplied {@link MultipartFile} contains an image of an
 * unsupported format.
 */
public class ImageFormatException extends FileException {

    /**
     * Constructs a {@link ImageFormatException} with a default custom message and
     * the specified {@code cause}.
     * 
     * @param cause The root cause.
     */
    public ImageFormatException(Throwable cause) {
        super(ValidationMessageConfig.IMAGE_FORMAT_UNSUPPORTED, cause);
    }

    /**
     * Constructs a {@link ImageFormatException} with a default custom message.
     */
    public ImageFormatException() {
        this(null);
    }

}
