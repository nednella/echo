package com.example.echo_api.exception.custom.badrequest;

/**
 * Thrown if there is an error when converting a HTTP request JSON payload into
 * an application entity.
 */
public class DeserializationException extends BadRequestException {

    /**
     * Constructs a {@code DeserializationException} with the specified
     * {@code message}.
     * 
     * @param message The specific error message.
     */
    public DeserializationException(String message) {
        super(message);
    }

}
