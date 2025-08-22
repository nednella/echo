package com.example.echo_api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.svix.Webhook;

import lombok.Setter;

/**
 * Config class for Svix webhook verification.
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "clerk")
public class SvixConfig {

    private String webhookSigningSecret;

    /**
     * Create an instance of the Svix Webhook verifier using the supplied
     * {@code webhookSigningSecret}.
     * 
     * @return svix webhook verifier instance
     */
    @Bean
    @ConditionalOnProperty(prefix = "clerk", name = "webhook-signing-secret")
    public Webhook webhook() {
        return new Webhook(webhookSigningSecret);
    }

}
