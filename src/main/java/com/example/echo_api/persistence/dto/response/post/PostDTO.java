package com.example.echo_api.persistence.dto.response.post;

import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardised response format for a single post.
 * 
 * @param id             The unique post id.
 * @param parentId       The id of the post that this post replies to, or NULL
 *                       if not a reply.
 * @param repostOfId     The id of the post that this post is a repost of, or
 *                       NULL if not a repost.
 * @param conversationId The id of the root post of the current conversation.
 *                       Equal to itself for root posts.
 * @param author         The author of the post.
 * @param text           The text content of the post, limited to 140
 *                       characters.
 * @param createdAt      The creation timestamp in ISO-8601 format.
 * @param metrics        The engagement metrics (likes, replies, shares).
 * @param relationship   The relationship between the requesting user and the
 *                       post (liked).
 * @param entities       The entities related to the post text (e.g., urls, user
 *                       mentions, hashtags).
 * @param isQuotedRepost Indicates the repost type. TRUE for quoted reposts,
 *                       FALSE for reposts, or NULL if not a repost.
 * @param originalPost   The post that this post is reposting, or NULL if not a
 *                       repost.
 */
// @formatter:off
public record PostDTO(
    String id,
    @JsonProperty("parent_id") String parentId,
    @JsonProperty("repost_of_id") String repostOfId,
    @JsonProperty("conversation_id") String conversationId,
    SimplifiedProfileDTO author,
    String text,
    @JsonProperty("created_at") String createdAt,
    PostMetricsDTO metrics,
    PostRelationshipDTO relationship,
    PostEntitiesDTO entities,
    @JsonProperty("is_quoted_post") @JsonInclude(Include.NON_NULL) boolean isQuotedRepost,
    @JsonProperty("original_post") @JsonInclude(Include.NON_NULL) PostDTO originalPost
) {}
// @formatter:on
