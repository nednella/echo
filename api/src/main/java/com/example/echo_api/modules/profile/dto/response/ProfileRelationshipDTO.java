package com.example.echo_api.modules.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

// @formatter:off
/**
 * Represents a standardised response format for profile relationships.
 *
 * @param following  indicates if the requesting profile follows the requested
 *                   profile
 * @param followedBy indicates if the requesting profile is followed by the
 *                   requested profile
 */
@Schema(
    name = "Profile Relationship",
    description = "Describes the relationship between the current user and the given profile."
)
public record ProfileRelationshipDTO(
    boolean following,
    @JsonProperty("followed_by") boolean followedBy
) {}
