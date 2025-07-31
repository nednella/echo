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

    private String secretKey;

    @Bean
    Clerk clerk() {
        return Clerk.builder().bearerAuth(secretKey).build();
    }

}
