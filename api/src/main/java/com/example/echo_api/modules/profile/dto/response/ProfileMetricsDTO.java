package com.example.echo_api.modules.profile.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

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
    name = "ProfileMetrics",
    description = "Contextual metrics related to the given profile.",
    accessMode = Schema.AccessMode.READ_ONLY
)
public record ProfileMetricsDTO(
    @NotNull int followers,
    @NotNull int following,
    @NotNull int posts
// int media // TODO: implement post media
) {}