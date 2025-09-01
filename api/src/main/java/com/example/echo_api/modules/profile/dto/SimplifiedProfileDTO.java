package com.example.echo_api.modules.profile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public record SimplifiedProfileDTO(
    String id,
    String username,
    String name,
    @JsonProperty("image_url") String imageUrl,
    @JsonInclude(Include.NON_NULL) ProfileRelationshipDTO relationship
) {}
