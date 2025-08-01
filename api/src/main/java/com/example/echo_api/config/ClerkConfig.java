package com.example.echo_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.clerk.backend_api.Clerk;

import lombok.Setter;

@Setter
@Configuration
@ConfigurationProperties(prefix = "clerk")
public class ClerkConfig {

    public static final String ECHO_ID = "echo_id";
    public static final String METADATA = "metadata";
    public static final String ONBOARDING_COMPLETE_KEY = "onboardingComplete";
    public static final boolean ONBOARDING_COMPLETE_VALUE = true;

    private String secretKey;

    @Bean
    Clerk clerk() {
        return Clerk
            .builder()
            .bearerAuth(secretKey)
            .build();
    }

}
