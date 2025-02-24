package com.example.echo_api.exception.custom.image;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.persistence.model.image.Image;
import com.example.echo_api.persistence.repository.ImageRepository;

/**
 * Thrown if an {@link Image} is not found within the {@link ImageRepository}.
 */
public class ImageNotFoundException extends ImageException {

    /**
     * Constructs a {@link ImageNotFoundException} with a default custom message and
     * the specified {@code cause}.
     * 
     * @param cause The root cause.
     */
    public ImageNotFoundException(Throwable cause) {
        super(ErrorMessageConfig.IMAGE_NOT_FOUND, cause);
    }

    /**
     * Constructs a {@link ImageNotFoundException} with a default custom message.
     */
    public ImageNotFoundException() {
        this(null);
    }

}
