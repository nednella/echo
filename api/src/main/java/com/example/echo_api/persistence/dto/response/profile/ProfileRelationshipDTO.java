package com.example.echo_api.persistence.dto.response.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardised response format for profile relationships.
 *
 * @param following  Indicates if the requesting profile follows the requested
 *                   profile.
 * @param followedBy Indicates if the requesting profile is followed by the
 *                   requested profile.
 * @param blocking   Indicates if the requesting profile blocks the requested
 *                   profile. // TODO: remove field
 * @param blockedBy  Indicates if the requesting profile is blocked by the
 *                   requested profile. // TODO: remove field
 */
// @formatter:off
public record ProfileRelationshipDTO(
    boolean following,
    @JsonProperty("followed_by") boolean followedBy,
    boolean blocking, // TODO: remove field
    @JsonProperty("blocked_by") boolean blockedBy // TODO: remove field
) {}
// @formatter:on
