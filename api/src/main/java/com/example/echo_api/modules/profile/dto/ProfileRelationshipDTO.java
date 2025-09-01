package com.example.echo_api.modules.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardised response format for profile relationships.
 *
 * @param following  indicates if the requesting profile follows the requested
 *                   profile
 * @param followedBy indicates if the requesting profile is followed by the
 *                   requested profile
 */
public record ProfileRelationshipDTO(
    boolean following,
    @JsonProperty("followed_by") boolean followedBy
) {}
