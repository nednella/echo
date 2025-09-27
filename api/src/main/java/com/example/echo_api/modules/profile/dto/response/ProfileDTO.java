package com.example.echo_api.modules.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// @formatter:off
/**
 * Represents a standardised response format for a user profile.
 *
 * @param id           the id of the user associated to the profile
 * @param username     the username of the user associated to the profile
 * @param name         the profile name (nullable)
 * @param bio          the profile bio (nullable)
 * @param location     the profile location (nullable)
 * @param imageUrl     the URL of the profile avatar image (nullable)
 * @param createdAt    the timestamp when the profile was created (ISO-8601
 *                     format)
 * @param metrics      the profile metrics
 * @param relationship the relationships between the profile and the
 *                     authenticated user (nullable)
 */
@Schema(
    name = "Profile",
    description = "A complete representation of a single profile."
)
public record ProfileDTO(
    @NotNull String id,
    @NotNull String username,
    String name,
    String bio,
    String location,
    @JsonProperty("image_url") String imageUrl,
    @NotNull @JsonProperty("created_at") String createdAt,
    @NotNull ProfileMetricsDTO metrics,
    @JsonInclude(Include.NON_NULL) ProfileRelationshipDTO relationship
) {}
