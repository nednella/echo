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
 *                   profile.
 * @param blockedBy  Indicates if the requesting profile is blocked by the
 *                   requested profile.
 */
// @formatter:off
public record RelationshipDTO(
    boolean following,
    @JsonProperty("followed_by") boolean followedBy,
    boolean blocking,
    @JsonProperty("blocked_by") boolean blockedBy
) {}
// @formatter:on
