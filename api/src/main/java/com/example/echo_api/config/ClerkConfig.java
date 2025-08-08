package com.example.echo_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.clerk.backend_api.Clerk;

import lombok.Setter;

/**
 * Config class for Clerk authentication integration.
 * 
 * <p>
 * Centralises Clerk-related configuration including:
 * 
 * <ul>
 * <li>Clerk JWT claim constants
 * <li>Expected onboarding metadata K/V pair
 * <li>Clerk SDK instance configuration
 * </ul>
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "clerk")
public class ClerkConfig {

    public static final String ECHO_ID = "echo_id";
    public static final String METADATA = "metadata";
    public static final String ONBOARDING_COMPLETE_KEY = "onboardingComplete";
    public static final boolean ONBOARDING_COMPLETE_VALUE = true;

    private String secretKey;

    /**
     * Create an instance of the Clerk SDK using the supplied {@code secretKey}.
     * 
     * @return Clerk SDK instance
     */
    @Bean
    Clerk clerk() {
        return Clerk
            .builder()
            .bearerAuth(secretKey)
            .build();
    }

}
