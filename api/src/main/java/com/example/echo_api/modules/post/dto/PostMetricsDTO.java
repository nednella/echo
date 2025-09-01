package com.example.echo_api.modules.post.dto;

/**
 * Represents a standardised response format for post engagement metrics.
 *
 * @param likes   the number of likes the post has
 * @param replies the number of replies the post has
 * @param shares  the number of shares the post has
 */
public record PostMetricsDTO(
    int likes,
    int replies
// int shares // TODO: implement post shares
) {}
