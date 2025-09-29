package com.example.echo_api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Profile("!test") // Disable for tests - autoconfigured with Testcontainers
@Data
@Validated
@ConfigurationProperties("spring.datasource")
public class DataSourceProperties {

    @NotBlank(message = "Required DATASOURCE_URL value is missing from environment variables")
    String url;

    @NotBlank(message = "Required DATASROUCE_USERNAME value is missing from environment variables")
    String username;

    @NotBlank(message = "Required DATASOURCE_PASSWORD value is missing from environment variables")
    String password;

}
