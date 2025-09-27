package com.example.echo_api.modules.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// @formatter:off
/**
 * Represents a standardised response format for post relationships.
 *
 * @param liked indicates if the requesting user has liked the requested post
 */
@Schema(
    name = "PostRelationship",
    description = "Describes the relationship between the current user and the given post.",
    accessMode = Schema.AccessMode.READ_ONLY
)
public record PostRelationshipDTO(
    @NotNull boolean liked
) {}
