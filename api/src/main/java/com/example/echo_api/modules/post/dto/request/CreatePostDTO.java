package com.example.echo_api.modules.post.dto.request;

import java.util.UUID;

import com.example.echo_api.modules.profile.constant.ProfileConstraints;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// @formatter:off
/**
 * Represents a request to create a post.
 * 
 * @param parentId the id of the parent post, null if not a reply (nullable)
 * @param text     the text content of the post
 */
@Schema(
    name = "CreatePostRequest",
    description = "Represents the request body required to create a new post.",
    accessMode = Schema.AccessMode.WRITE_ONLY
)
public record CreatePostDTO(

    @JsonProperty("parent_id") UUID parentId,

    @NotBlank(message = "Post text cannot be null or blank")
    @Size(max = ProfileConstraints.TEXT_MAX_LENGTH, message = "Post text must not exceed {max} characters")
    String text

) {}
