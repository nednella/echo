package com.example.echo_api.persistence.dto.response.post;

/**
 * Represents a standardised response format for post engagement metrics.
 *
 * @param likes   The number of likes the post has.
 * @param replies The number of replies the post has.
 * @param shares  The number of shares the post has.
 */
// @formatter:off
public record PostMetricsDTO(
    int likes,
    int replies,
    int shares
) {}
// @formatter:on
