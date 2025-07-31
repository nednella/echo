package com.example.echo_api.exception.custom.badrequest;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the user with the specified
 * Clerk ID has not completed the onboarding process.
 */
public class OnboardingIncompleteException extends BadRequestException {

    /**
     * Constructs a {@code OnboardingIncompleteException} with a default message.
     */
    public OnboardingIncompleteException() {
        super(ErrorMessageConfig.BadRequest.ONBOARDING_NOT_COMPLETED);
    }
}
