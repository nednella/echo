package com.example.echo_api.modules.profile.dto.request;

import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.modules.post.constants.PostConstraints;

import jakarta.validation.constraints.Size;

/**
 * Represents a request to update the authenticated user's profile information.
 * 
 * @param name     the name for the profile
 * @param bio      the bio for the profile
 * @param location the location for the profile
 */
// @formatter:off
public record UpdateProfileDTO(

    @Size(max = PostConstraints.NAME_MAX_LENGTH, message = ValidationMessageConfig.NAME_TOO_LONG)
    String name,

    @Size(max = PostConstraints.BIO_MAX_LENGTH, message = ValidationMessageConfig.BIO_TOO_LONG)
    String bio,

    @Size(max = PostConstraints.LOCATION_MAX_LENGTH, message = ValidationMessageConfig.LOCATION_TOO_LONG)
    String location
    
) {}
// @formatter:on
