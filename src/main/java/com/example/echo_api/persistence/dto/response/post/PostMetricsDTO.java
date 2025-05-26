package com.example.echo_api.persistence.dto.response.post;

/**
 * Represents a standardised response format for post metrics.
 *
 * @param likes   The number of likes this post has.
 * @param replies The number of replies this post has.
 * @param shares  The number of shares this post has.
 */
// @formatter:off
public record PostMetricsDTO(
    int likes,
    int replies,
    int shares
) {}
// @formatter:on
