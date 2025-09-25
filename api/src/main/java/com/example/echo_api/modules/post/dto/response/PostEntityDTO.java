package com.example.echo_api.modules.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

// @formatter:off
/**
 * Represents a standardised response format for singular post entities
 * (hashtags, user mentions, urls), so that the frontend can easily create
 * clickable links within post bodies.
 */
@Schema(
    name = "Post Entity",
    description = "A single entity in the given post's text."
)
public record PostEntityDTO(
    int start,
    int end,
    String text
) {}
