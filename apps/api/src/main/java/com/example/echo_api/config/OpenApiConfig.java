package com.example.echo_api.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

// @formatter:off
@OpenAPIDefinition(
    info = @Info(
        title = "Echo API",
        description = "A REST API implementation to serve the Echo client.",
        version = "0.3.1" // x-release-please-version
    )
)
@Configuration
public class OpenApiConfig {}
