package com.example.echo_api.modules.profile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardised response format for a user profile.
 *
 * @param id           the id of the user associated to the profile
 * @param username     the username of the user associated to the profile
 * @param imageUrl     the URL of the profile avatar image
 * @param name         the profile name
 * @param bio          the profile bio
 * @param location     the profile location
 * @param createdAt    the timestamp when the profile was created (ISO-8601
 *                     format)
 * @param metrics      the profile metrics
 * @param relationship the profile relationship between the requesting and the
 *                     requested profiles. Null if those profiles are the same
 */
public record ProfileDTO(
    String id,
    String username,
    String name,
    String bio,
    String location,
    @JsonProperty("image_url") String imageUrl,
    @JsonProperty("created_at") String createdAt,
    ProfileMetricsDTO metrics,
    @JsonInclude(Include.NON_NULL) ProfileRelationshipDTO relationship
) {}
