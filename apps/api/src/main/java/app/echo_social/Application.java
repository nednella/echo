package app.echo_social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

// @formatter:off
@SpringBootApplication
@ConfigurationPropertiesScan
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Clerk-issued JWT bearer token. Must include the 'echo_id' and 'onboarded' claims."
)
@OpenAPIDefinition(
    info = @Info(
        title = "Echo API",
        description = "A REST API implementation to serve the Echo client.",
        version = "0.4.0" // x-release-please-version
    ),
    servers = {
        @Server(url = "https://api.echo-social.app", description = "Production server"),
        @Server(url = "http://localhost:8080", description = "Development server")
    },
    security = @SecurityRequirement(name = "bearerAuth")
)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
