package com.example.echo_api.persistence.dto.response.post;

import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardised response format for a top-level user post as part
 * of a feed.
 * 
 * @param id           The post id.
 * @param parentId     The post id that this post is in reply to. Null if not a
 *                     reply.
 * @param author       The post author.
 * @param text         The post content.
 * @param createdAt    The timestamp when the post was created (ISO-8601
 *                     format).
 * @param metrics      The post metrics.
 * @param relationship The post relationship between the requesting profile and
 *                     the requested post.
 * 
 */
// @formatter:off
public record PostDTO(
    String id,
    @JsonProperty("parent_id") String parentId,
    SimplifiedProfileDTO author,
    String text,
    @JsonProperty("created_at") String createdAt,
    PostMetricsDTO metrics,
    PostRelationshipDTO relationship
) {}
// @formatter:on
