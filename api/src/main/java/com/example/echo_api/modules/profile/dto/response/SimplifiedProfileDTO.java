package com.example.echo_api.modules.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// @formatter:off
/**
 * Represents a simplified response format for a user profile, to be returned to
 * the client alongside larger entities, such as posts and comments.
 *
 * @param id           the id of the user associated to the profile
 * @param username     the username of the user associated to the profile
 * @param name         the profile name (nullable)
 * @param imageUrl     the URL of the profile avatar image (nullable)
 * @param relationship the relationships between the profile and the
 *                     authenticated user (nullable)
 */
@Schema(
    name = "SimplifiedProfile",
    description = "A lightweight representation of a profile, used within contextual objects like posts or lists of followers/following.",
    accessMode = Schema.AccessMode.READ_ONLY
)
public record SimplifiedProfileDTO(
    @NotNull String id,
    @NotNull String username,
    String name,
    @JsonProperty("image_url") String imageUrl,
    @JsonInclude(Include.NON_NULL) ProfileRelationshipDTO relationship
) {}
