package com.example.echo_api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Validated
@ConfigurationProperties("cloudinary")
public class CloudinaryProperties {

    @NotBlank(message = "Required CLOUDINARY_CLOUD_NAME value is missing from environment variables")
    String cloudName;

    @NotBlank(message = "Required CLOUDINARY_API_KEY value is missing from environment variables")
    String apiKey;

    @NotBlank(message = "Required CLOUDINARY_API_SECRET value is missing from environment variables")
    String apiSecret;

}
