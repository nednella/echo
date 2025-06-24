package com.example.echo_api.persistence.dto.request.profile;

import com.example.echo_api.config.ConstraintsConfig;
import com.example.echo_api.config.ValidationMessageConfig;

import jakarta.validation.constraints.Size;

/**
 * Represents a request to update the authenticated account profile information.
 * 
 * @param name     The name for the account profile.
 * @param bio      The bio for the account profile.
 * @param location The location for the account profile.
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
