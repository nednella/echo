package com.example.echo_api.exception.custom.badrequest;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;

/**
 * Thrown if a supplied {@link MultipartFile} is invalid in any way.
 */
public class InvalidFileException extends BadRequestException {

    /**
     * Constructs a {@link FileInvalidException} with a default message and details.
     */
    public InvalidFileException() {
        super(ErrorMessageConfig.BadRequest.INVALID_REQUEST, ValidationMessageConfig.INVALID_FILE);
    }

}
