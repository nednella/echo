package com.example.echo_api.exception.custom.unauthorised;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if there is a request made from a source that is missing or has bad
 * authentication with the server.
 */
public class UnauthorisedRequestException extends UnauthorisedException {

    /**
     * Constructs a {@link UnauthorisedRequestException} with a default custom
     * message.
     */
    public UnauthorisedRequestException() {
        super(ErrorMessageConfig.Unauthorised.UNAUTHORISED_REQUEST);
    }

}
