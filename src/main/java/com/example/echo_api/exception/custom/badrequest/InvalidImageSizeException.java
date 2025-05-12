package com.example.echo_api.exception.custom.badrequest;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;

/**
 * Thrown if a supplied {@link MultipartFile} contains an image of a size larger
 * than the maximum allowed.
 */
public class InvalidImageSizeException extends BadRequestException {

    /**
     * Constructs a {@link InvalidImageSizeException} with a default custom message
     * and details.
     */
    public InvalidImageSizeException() {
        super(ErrorMessageConfig.BadRequest.INVALID_REQUEST, ValidationMessageConfig.IMAGE_SIZE_TOO_LARGE);
    }

}
