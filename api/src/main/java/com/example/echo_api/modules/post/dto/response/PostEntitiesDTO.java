package com.example.echo_api.modules.post.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

// @formatter:off
/**
 * Represents a standardised response format for groups of post entities.
 */
@Schema(
    name = "Post Entities",
    description = "Collections of parsed entities in the given post's text."
)
public record PostEntitiesDTO(
    // List<?> media, // TODO: implement post images
    List<PostEntityDTO> hashtags,
    List<PostEntityDTO> mentions,
    List<PostEntityDTO> urls
) {}
