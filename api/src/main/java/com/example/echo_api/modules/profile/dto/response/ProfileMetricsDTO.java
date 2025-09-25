package com.example.echo_api.modules.profile.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

// @formatter:off
/**
 * Represents a standardised response format for profile metrics.
 *
 * @param followers the number of followers this profile has
 * @param following the number of profiles this profile is following
 * @param posts     the number of posts this profile has made
 * @param media     the number of media items this profile has uploaded
 */
@Schema(
    name = "Profile Metrics",
    description = "Contextual metrics related to the given profile."
)
public record ProfileMetricsDTO(
    int followers,
    int following,
    int posts
// int media // TODO: implement post media
) {}