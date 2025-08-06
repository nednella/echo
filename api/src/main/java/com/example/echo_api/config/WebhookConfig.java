package com.example.echo_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.svix.Webhook;

import lombok.Setter;

@Setter
@Configuration
@ConfigurationProperties(prefix = "clerk.webhook")
public class WebhookConfig {

    private String signingSecret;

    @Bean
    public Webhook webhook() {
        return new Webhook(signingSecret);
    }

}
