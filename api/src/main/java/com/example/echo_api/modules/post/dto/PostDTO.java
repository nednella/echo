package com.example.echo_api.modules.post.dto;

import com.example.echo_api.modules.profile.dto.SimplifiedProfileDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardised response format for a single post.
 * 
 * @param id             the unique post id
 * @param parentId       the id of the post that this post replies to, or null
 *                       if a root post
 * @param conversationId the id of the root post of the conversation, equal to
 *                       itself for root posts
 * @param author         the author of the post
 * @param text           the text content of the post, limited to 140 characters
 * @param createdAt      the creation timestamp in ISO-8601 format
 * @param metrics        the engagement metrics (likes, replies, shares)
 * @param relationship   the relationship between the requesting user and the
 *                       post (liked)
 * @param entities       the entities related to the post text (e.g., urls, user
 *                       mentions, hashtags)
 */
public record PostDTO(
    String id,
    @JsonProperty("parent_id") String parentId,
    @JsonProperty("conversation_id") String conversationId,
    SimplifiedProfileDTO author,
    String text,
    @JsonProperty("created_at") String createdAt,
    PostMetricsDTO metrics,
    PostRelationshipDTO relationship,
    PostEntitiesDTO entities
) {}
