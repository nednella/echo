package com.example.echo_api.persistence.dto.response.post;

/**
 * Represents a standardised response format for singular post entities
 * (hashtags, user mentions, urls), so that the frontend can easily create
 * clickable links within post bodies.
 */
// @formatter:off
public record PostEntityDTO(
    int start,
    int end,
    String text
) {}
// @formatter:on
