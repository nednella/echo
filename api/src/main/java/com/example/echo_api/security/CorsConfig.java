package com.example.echo_api.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.Setter;

/**
 * Configuration class for Spring Security.
 * 
 * <p>
 * The class configures the Cross-Origin Resouce Sharing policies for the
 * application.
 * 
 * <p>
 * The policy allows all header types and HTTP methods, and dynamically assigns
 * allowed origins as specified by application.yml.
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsConfig {

    /**
     * List of allowed origins for CORS requests.
     */
    private List<String> allowedOrigins;

    /**
     * Creates and returns a {@link CorsConfigurationSource} that defines the CORS
     * policy for the application.
     * 
     * @return the configured {@link CorsConfigurationSource} bean that is
     *         automatically picked up by Spring Security.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        allowedOrigins.forEach(config::addAllowedOrigin);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
