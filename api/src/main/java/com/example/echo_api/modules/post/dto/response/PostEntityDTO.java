package com.example.echo_api.modules.post.dto.response;

/**
 * Represents a standardised response format for singular post entities
 * (hashtags, user mentions, urls), so that the frontend can easily create
 * clickable links within post bodies.
 */
public record PostEntityDTO(
    int start,
    int end,
    String text
) {}
