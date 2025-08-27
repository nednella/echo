package com.example.echo_api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Validated
@ConfigurationProperties("clerk")
public class ClerkProperties {

    @NotBlank(message = "Required CLERK_SECRET_KEY value is missing from environment variables")
    String secretKey;

    @NotBlank(message = "Required CLERK_WEBHOOK_SIGNING_SECRET value is missing from environment variables")
    String webhookSigningSecret;

}
