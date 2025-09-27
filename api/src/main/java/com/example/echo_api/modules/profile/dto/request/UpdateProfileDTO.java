package com.example.echo_api.modules.profile.dto.request;

import com.example.echo_api.modules.post.constant.PostConstraints;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

// @formatter:off
/**
 * Represents the request body required to update a user's profile information.
 * 
 * @param name     the name for the profile
 * @param bio      the bio for the profile
 * @param location the location for the profile
 */
@Schema(
    name = "UpdateProfileRequest",
    description = "Represents the request body required to update a user's profile information."
)
public record UpdateProfileDTO(

    @Size(max = PostConstraints.NAME_MAX_LENGTH, message = "Name must not exceed {max} characters")
    String name,

    @Size(max = PostConstraints.BIO_MAX_LENGTH, message = "Bio must not exceed {max} characters")
    String bio,
    
    @Size(max = PostConstraints.LOCATION_MAX_LENGTH, message = "Location must not exceed {max} characters")
    String location
    
) {}
