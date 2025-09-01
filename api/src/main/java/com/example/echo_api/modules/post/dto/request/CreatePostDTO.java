package com.example.echo_api.modules.post.dto.request;

import java.util.UUID;

import com.example.echo_api.config.ConstraintsConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents a request to create a post.
 * 
 * @param parentId the id of the parent post, null if not a reply
 * @param text     the text content of the post (required)
 */
// @formatter:off
public record CreatePostDTO(

    @JsonProperty("parent_id") UUID parentId,

    @NotBlank(message = ValidationMessageConfig.TEXT_NULL_OR_BLANK)
    @Size(max = ConstraintsConfig.Post.TEXT_MAX_LENGTH, message = ValidationMessageConfig.TEXT_TOO_LONG)
    String text

) {}
// @formatter:on
