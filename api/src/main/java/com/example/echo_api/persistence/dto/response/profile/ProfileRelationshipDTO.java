package com.example.echo_api.persistence.dto.response.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardised response format for profile relationships.
 *
 * @param following  indicates if the requesting profile follows the requested
 *                   profile
 * @param followedBy indicates if the requesting profile is followed by the
 *                   requested profile
 */
// @formatter:off
public record ProfileRelationshipDTO(
    boolean following,
    @JsonProperty("followed_by") boolean followedBy
) {}
// @formatter:on
