package com.example.echo_api.exception.custom.badrequest;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if a HTTP request is rejected because the onboarding attempt fails due
 * to the user's session token containing {@code onboardingComplete} metadata
 * equal to {@code true}.
 */
public class OnboardingCompleteException extends BadRequestException {

    /**
     * Constructs a {@code OnboardingCompleteException} with a default message.
     */
    public OnboardingCompleteException() {
        super(ErrorMessageConfig.BadRequest.ONBOARDING_COMPLETED);
    }

}
