package com.example.echo_api.exception.custom.unauthorised;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if there is an error when verifying a webhook message delivered to the
 * application using Svix.
 */
public class WebhookVerificationException extends UnauthorisedException {

    /**
     * Constructs a {@link WebhookVerificationException} with a default message.
     */
    public WebhookVerificationException() {
        super(ErrorMessageConfig.Unauthorised.INVALID_WEBHOOK_SIGNATURE);
    }

}
