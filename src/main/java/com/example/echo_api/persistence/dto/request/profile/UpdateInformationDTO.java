package com.example.echo_api.persistence.dto.request.profile;

import com.example.echo_api.config.ValidationMessageConfig;

import jakarta.validation.constraints.Size;

/**
 * Represents a request to update the authenticated account profile information.
 * 
 * @param name     The name for the account profile. Must not exceed 50
 *                 characters.
 * @param bio      The bio for the account profile. Must not exceed 160
 *                 characters.
 * @param location The location for the account profile. Must not exceed 30
 *                 characters.
 */
// @formatter:off
public record UpdateInformationDTO(

    @Size(max = 50, message = ValidationMessageConfig.NAME_TOO_LONG)
    String name,

    @Size(max = 160, message = ValidationMessageConfig.BIO_TOO_LONG)
    String bio,

    @Size(max = 30, message = ValidationMessageConfig.LOCATION_TOO_LONG)
    String location
    
) {}
// @formatter:on
