package com.example.echo_api.persistence.dto.response.profile;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardised response format for a user profile.
 *
 * @param username     The username of the account associated to the profile.
 * @param name         The profile name.
 * @param bio          The profile bio.
 * @param location     The profile location.
 * @param avatarUrl    The URL of the profile avatar image.
 * @param bannerUrl    The URL of the profile banner image.
 * @param createdAt    The timestamp when the profile was created (ISO-8601
 *                     format).
 * @param metrics      The profile metrics.
 * @param relationship The profile relationship between the requesting and the
 *                     requested profiles.
 */
// @formatter:off
public record ProfileDTO(
    String username,
    String name,
    String bio,
    String location,
    @JsonProperty("avatar_url") String avatarUrl,
    @JsonProperty("banner_url") String bannerUrl,
    @JsonProperty("created_at") String createdAt,
    @JsonProperty("profile_metrics") MetricsDTO metrics,
    @JsonProperty("relationship") @JsonInclude(Include.NON_NULL) RelationshipDTO relationship
) {

    public ProfileDTO(
        String username,
        String name,
        String bio,
        String location,
        String avatarUrl,
        String bannerUrl,
        LocalDateTime createdAt,
        MetricsDTO metrics,
        RelationshipDTO relationship
    ) {
        this(
            username,
            name,
            bio,
            location,
            avatarUrl,
            bannerUrl,
            createdAt != null ? createdAt.toString() : null,
            metrics,
            relationship
        );
    }

}
// @formatter:on
