package com.example.echo_api.persistence.dto.response.post;

import java.util.List;

/**
 * Represents a standardised response format for groups of post entities.
 */
// @formatter:off
public record PostEntitiesDTO(
    // List<?> media, // TODO: implement post images
    List<PostEntityDTO> hashtags,
    List<PostEntityDTO> mentions,
    List<PostEntityDTO> urls
) {}
// @formatter:on
