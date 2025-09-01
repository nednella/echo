package com.example.echo_api.modules.post.dto;

import java.util.List;

/**
 * Represents a standardised response format for groups of post entities.
 */
public record PostEntitiesDTO(
    // List<?> media, // TODO: implement post images
    List<PostEntityDTO> hashtags,
    List<PostEntityDTO> mentions,
    List<PostEntityDTO> urls
) {}
