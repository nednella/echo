package com.example.echo_api.exception.custom.file;

import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.config.ValidationMessageConfig;

/**
 * Thrown if a supplied {@link MultipartFile} contains an image of a size larger
 * than the maximum allowed.
 */
public class ImageSizeException extends FileException {

    /**
     * Constructs a {@link ImageSizeException} with a default custom message and the
     * specified {@code cause}.
     * 
     * @param cause The root cause.
     */
    public ImageSizeException(Throwable cause) {
        super(ValidationMessageConfig.IMAGE_SIZE_TOO_LARGE, cause);
    }

    /**
     * Constructs a {@link ImageSizeException} with a default custom message.
     */
    public ImageSizeException() {
        this(null);
    }

}
