package com.example.echo_api.modules.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// @formatter:off
/**
 * Represents a standardised response format for singular post entities
 * (hashtags, user mentions, urls), so that the frontend can easily create
 * clickable links within post bodies.
 */
@Schema(
    name = "PostEntity",
    description = "A single entity in the given post's text.",
    accessMode = Schema.AccessMode.READ_ONLY
)
public record PostEntityDTO(
    @NotNull int start,
    @NotNull int end,
    @NotNull String text
) {}
