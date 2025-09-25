package com.example.echo_api.modules.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

// @formatter:off
/**
 * Represents a standardised response format for post engagement metrics.
 *
 * @param likes   the number of likes the post has
 * @param replies the number of replies the post has
 * @param shares  the number of shares the post has
 */
@Schema(
    name = "Post Metrics",
    description = "Engagement metrics for the given post."
)
public record PostMetricsDTO(
    int likes,
    int replies
// int shares // TODO: implement post shares
) {}
