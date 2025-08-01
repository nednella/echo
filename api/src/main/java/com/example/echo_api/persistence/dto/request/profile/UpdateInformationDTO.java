package com.example.echo_api.persistence.dto.request.profile;

import com.example.echo_api.config.ConstraintsConfig;
import com.example.echo_api.config.ValidationMessageConfig;

import jakarta.validation.constraints.Size;

/**
 * Represents a request to update the authenticated user's profile information.
 * 
 * @param name     The name for the profile.
 * @param bio      The bio for the profile.
 * @param location The location for the profile.
 */
// @formatter:off
public record UpdateInformationDTO(

    @Size(max = ConstraintsConfig.Profile.NAME_MAX_LENGTH, message = ValidationMessageConfig.NAME_TOO_LONG)
    String name,

    @Size(max = ConstraintsConfig.Profile.BIO_MAX_LENGTH, message = ValidationMessageConfig.BIO_TOO_LONG)
    String bio,

    @Size(max = ConstraintsConfig.Profile.LOCATION_MAX_LENGTH, message = ValidationMessageConfig.LOCATION_TOO_LONG)
    String location
    
) {}
// @formatter:on
