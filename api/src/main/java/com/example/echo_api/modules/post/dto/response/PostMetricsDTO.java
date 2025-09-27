package com.example.echo_api.modules.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// @formatter:off
/**
 * Represents a standardised response format for post engagement metrics.
 *
 * @param likes   the number of likes the post has
 * @param replies the number of replies the post has
 * @param shares  the number of shares the post has
 */
@Schema(
    name = "PostMetrics",
    description = "Engagement metrics for the given post.",
    accessMode = Schema.AccessMode.READ_ONLY
)
public record PostMetricsDTO(
    @NotNull int likes,
    @NotNull int replies
// int shares // TODO: implement post shares
) {}
