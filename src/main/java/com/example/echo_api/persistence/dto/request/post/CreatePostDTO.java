package com.example.echo_api.persistence.dto.request.post;

import java.util.UUID;

import com.example.echo_api.config.ConstraintsConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents a request to create a post.
 * 
 * @param parentId The id of the parent post. Null if not a reply.
 * @param text     The text content of the post. Required field.
 */
// @formatter:off
public record CreatePostDTO(

    @JsonProperty("parent_id")
    UUID parentId,

    @NotBlank(message = ValidationMessageConfig.TEXT_NULL_OR_BLANK)
    @Size(max = ConstraintsConfig.Post.TEXT_MAX_LENGTH, message = ValidationMessageConfig.TEXT_TOO_LONG)
    String text

) {}
// @formatter:on
