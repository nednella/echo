package com.example.echo_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.echo_api.config.properties.ClerkProperties;
import com.svix.Webhook;

import lombok.RequiredArgsConstructor;

/**
 * Config class for Svix webhook verification.
 */
@Configuration
@RequiredArgsConstructor
public class SvixConfig {

    private final ClerkProperties props;

    /**
     * Create an instance of the Svix Webhook verifier using the supplied
     * {@code webhookSigningSecret}.
     * 
     * @return svix webhook verifier instance
     */
    @Bean
    public Webhook webhook() {
        return new Webhook(props.getWebhookSigningSecret());
    }

}
