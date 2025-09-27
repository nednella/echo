package com.example.echo_api.modules.post.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// @formatter:off
/**
 * Represents a standardised response format for groups of post entities.
 */
@Schema(
    name = "PostEntities",
    description = "Collections of parsed entities in the given post's text."
)
public record PostEntitiesDTO(
    // List<?> media, // TODO: implement post images
    @NotNull List<PostEntityDTO> hashtags,
    @NotNull List<PostEntityDTO> mentions,
    @NotNull List<PostEntityDTO> urls
) {}
