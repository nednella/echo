package com.example.echo_api.persistence.dto.response.profile;

/**
 * Represents a standardised response format for profile metrics.
 *
 * @param followers The number of followers this profile has.
 * @param following The number of profiles this profile is following.
 * @param posts     The number of posts this profile has made.
 * @param media     The number of media items this profile has uploaded.
 */
// @formatter:off
public record ProfileMetricsDTO(
    int followers,
    int following,
    int posts
    // int media // TODO: implement post media
) {}
// @formatter:on
