package com.example.echo_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.svix.Webhook;

import lombok.Setter;

/**
 * Config class for Svix webhook verification using the Clerk webhook signing
 * secret.
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "clerk.webhook")
public class SvixConfig {

    private String signingSecret;

    /**
     * Create an instance of the Svix Webhook verifier using the supplied
     * {@code signingSecret}.
     * 
     * @return Svix webhook verifier instance
     */
    @Bean
    public Webhook webhook() {
        return new Webhook(signingSecret);
    }

}
