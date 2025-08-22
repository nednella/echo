package com.example.echo_api.config;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.svix.Webhook;

/**
 * Mock the Svix Webhook instance during integration testing, so that the real
 * bean doesn't throw an exception when it tries to instantiate with a null
 * signing secret.
 */
@TestConfiguration
public class SvixTestConfig {

    @Bean
    public Webhook webhook() {
        return mock(Webhook.class);
    }

}
