package com.example.echo_api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Validated
@ConfigurationProperties("spring.security.oauth2.resourceserver.jwt")
public class AuthenticationProperties {

    @NotBlank(message = "Required CLERK_ISSUER_URI value is missing from environment variables")
    String issuerUri;

    @NotBlank(message = "Required CLERK_JWK_SET_URI value is missing from environment variables")
    String jwkSetUri;

}
