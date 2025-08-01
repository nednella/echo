package com.example.echo_api.persistence.dto.response.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardised response format for a user profile.
 *
 * @param id           The id of the user associated to the profile.
 * @param username     The username of the user associated to the profile.
 * @param name         The profile name.
 * @param bio          The profile bio.
 * @param location     The profile location.
 * @param avatarUrl    The URL of the profile avatar image.
 * @param bannerUrl    The URL of the profile banner image.
 * @param createdAt    The timestamp when the profile was created (ISO-8601
 *                     format).
 * @param metrics      The profile metrics.
 * @param relationship The profile relationship between the requesting and the
 *                     requested profiles. Null if those profiles are the same.
 */
// @formatter:off
public record ProfileDTO(
    String id,
    String username,
    String name,
    String bio,
    String location,
    @JsonProperty("avatar_url") String avatarUrl,
    @JsonProperty("banner_url") String bannerUrl,
    @JsonProperty("created_at") String createdAt,
    ProfileMetricsDTO metrics,
    @JsonInclude(Include.NON_NULL) ProfileRelationshipDTO relationship
) {}
// @formatter:on
