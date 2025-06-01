package com.example.echo_api.exception.custom.badrequest;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;

/**
 * Thrown if a {@link Post} does not exist by the supplied {@link UUID}.
 */
public class InvalidParentIdException extends BadRequestException {

    /**
     * Constructs a {@link InvalidParentIdException} with a default message and
     * details.
     */
    public InvalidParentIdException() {
        super(ErrorMessageConfig.BadRequest.INVALID_REQUEST, ValidationMessageConfig.INVALID_PARENT_ID);
    }

}
