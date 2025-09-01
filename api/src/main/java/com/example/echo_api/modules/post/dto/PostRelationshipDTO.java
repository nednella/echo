package com.example.echo_api.modules.post.dto;

/**
 * Represents a standardised response format for post relationships.
 *
 * @param liked indicates if the requesting user has liked the requested post
 */
public record PostRelationshipDTO(
    boolean liked
) {}
