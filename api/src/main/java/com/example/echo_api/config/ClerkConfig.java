package com.example.echo_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.clerk.backend_api.Clerk;
import com.example.echo_api.config.properties.ClerkProperties;
import com.svix.Webhook;

import lombok.RequiredArgsConstructor;

/**
 * Config class for Clerk authentication integration.
 * 
 * <ul>
 * <li>Clerk JWT claim constants
 * <li>Clerk user public metadata onboarding status K/V pair
 * <li>Clerk SDK instance configuration
 * </ul>
 */
@Configuration
@RequiredArgsConstructor
public class ClerkConfig {

    public static final String JWT_ECHO_ID_CLAIM = "echo_id";
    public static final String JWT_ONBOARDED_CLAIM = "onboarded";

    public static final String ONBOARDING_COMPLETE_METADATA_KEY = "onboardingComplete";
    public static final boolean ONBOARDING_COMPLETE_METADATA_VALUE = true;

    private final ClerkProperties props;

    /**
     * Create an instance of the Clerk SDK using the supplied {@code secretKey}.
     * 
     * @return Clerk SDK instance
     */
    @Bean
    Clerk clerk() {
        return Clerk
            .builder()
            .bearerAuth(props.getSecretKey())
            .build();
    }

    /**
     * Create an instance of the Svix Webhook verifier using the supplied
     * {@code webhookSigningSecret}.
     * 
     * @return svix webhook instance
     */
    @Bean
    public Webhook webhook() {
        return new Webhook(props.getWebhookSigningSecret());
    }

}
