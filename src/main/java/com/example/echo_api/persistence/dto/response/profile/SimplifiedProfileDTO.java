package com.example.echo_api.persistence.dto.response.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a simplified response format for a user profile, to be returned to
 * the client alongside larger entities, such as posts and comments.
 *
 * @param id           The id of the account associated to the profile
 * @param username     The username of the account associated to the profile.
 * @param name         The profile name.
 * @param avatarUrl    The URL of the profile avatar image.
 * @param relationship The profile relationship between the requesting and the
 *                     requested profiles. Null if those profiles are the same.
 */
// @formatter:off
public record SimplifiedProfileDTO(
    String id,
    String username,
    String name,
    @JsonProperty("avatar_url") String avatarUrl,
    @JsonInclude(Include.NON_NULL) RelationshipDTO relationship
) {}
// @formatter:on
