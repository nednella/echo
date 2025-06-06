package com.example.echo_api.exception.custom.badrequest;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;

/**
 * Thrown if a supplied {@link MultipartFile} contains a file of an unsupported
 * format.
 */
public class InvalidImageFormatException extends BadRequestException {

    /**
     * Constructs a {@link ImageFormatException} with a default message and details.
     */
    public InvalidImageFormatException() {
        super(ErrorMessageConfig.BadRequest.INVALID_REQUEST, ValidationMessageConfig.IMAGE_FORMAT_UNSUPPORTED);
    }

}
