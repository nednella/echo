package com.example.echo_api.persistence.dto.response.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardised response format for profile metrics.
 *
 * @param followingCount The number of profiles this profile is following.
 * @param followerCount  The number of followers this profile has.
 * @param postCount      The number of posts this profile has made.
 * @param mediaCount     The number of media items this profile has uploaded.
 */
// @formatter:off
public record MetricsDTO(
    @JsonProperty("following_count") int followingCount,
    @JsonProperty("follower_count") int followerCount,
    @JsonProperty("post_count") int postCount,
    @JsonProperty("media_count") int mediaCount
) {}
// @formatter:on
