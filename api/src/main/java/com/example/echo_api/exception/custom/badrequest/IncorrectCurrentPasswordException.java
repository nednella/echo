package com.example.echo_api.exception.custom.badrequest;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.persistence.dto.request.account.UpdatePasswordDTO;

/**
 * Thrown if an {@link UpdatePasswordDTO} is rejected because the supplied
 * current password does not match the hashed password.
 */
public class IncorrectCurrentPasswordException extends BadRequestException {

    /**
     * Constructs a {@code IncorrectCurrentPasswordException} with a default
     * message.
     */
    public IncorrectCurrentPasswordException() {
        super(ErrorMessageConfig.BadRequest.INCORRECT_CURRENT_PASSWORD);
    }

}
