package com.example.echo_api.persistence.dto.response.post;

import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardised response format for a single post.
 * 
 * @param id             The unique post id.
 * @param parentId       The id of the post that this post replies to, or null
 *                       if a root post.
 * @param conversationId The id of the root post of the conversation, equal to
 *                       itself for root posts.
 * @param author         The author of the post.
 * @param text           The text content of the post, limited to 140
 *                       characters.
 * @param createdAt      The creation timestamp in ISO-8601 format.
 * @param metrics        The engagement metrics (likes, replies, shares).
 * @param relationship   The relationship between the requesting user and the
 *                       post (liked).
 */
// @formatter:off
public record PostDTO(
    String id,
    @JsonProperty("parent_id") String parentId,
    @JsonProperty("conversation_id") String conversationId,
    SimplifiedProfileDTO author,
    String text,
    @JsonProperty("created_at") String createdAt,
    PostMetricsDTO metrics,
    PostRelationshipDTO relationship
) {}
// @formatter:on
