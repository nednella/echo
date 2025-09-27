package com.example.echo_api.modules.post.dto.response;

import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// @formatter:off
/**
 * Represents a standardised response format for a single post.
 * 
 * @param id             the unique post id
 * @param parentId       the id of the post that this post replies to (nullable)
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
@Schema(
    name = "Post",
    description = "A complete representation of a single post.",
    accessMode = Schema.AccessMode.READ_ONLY
)
public record PostDTO(
    @NotNull String id,
    @JsonProperty("parent_id") String parentId,
    @NotNull @JsonProperty("conversation_id") String conversationId,
    @NotNull SimplifiedProfileDTO author,
    @NotNull String text,
    @NotNull @JsonProperty("created_at") String createdAt,
    @NotNull PostMetricsDTO metrics,
    @NotNull PostRelationshipDTO relationship,
    @NotNull PostEntitiesDTO entities
) {}
