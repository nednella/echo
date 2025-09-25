package com.example.echo_api.modules.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

// @formatter:off
/**
 * Represents a simplified response format for a user profile, to be returned to
 * the client alongside larger entities, such as posts and comments.
 *
 * @param id           the id of the user associated to the profile
 * @param username     the username of the user associated to the profile
 * @param imageUrl     the URL of the profile avatar image
 * @param name         the profile name
 * @param relationship the relationships between the profile and the
 *                     authenticated user, null if same user id
 */
@Schema(
    name = "Simplified Profile",
    description = "A lightweight representation of a profile, used within contextual objects like posts or lists of followers/following."
)
public record SimplifiedProfileDTO(
    String id,
    String username,
    String name,
    @JsonProperty("image_url") String imageUrl,
    @JsonInclude(Include.NON_NULL) ProfileRelationshipDTO relationship
) {}
