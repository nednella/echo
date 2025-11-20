package com.example.echo_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

// @formatter:off
@SpringBootApplication
@ConfigurationPropertiesScan
@OpenAPIDefinition(
    info = @Info(
        title = "Echo API",
        description = "A REST API implementation to serve the Echo client.",
        version = "0.4.0" // x-release-please-version
    ),
    servers = {
        @Server(url = "https://api.echo-social.app", description = "Production server"),
        @Server(url = "http://localhost:8080", description = "Development server")
    }
)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
