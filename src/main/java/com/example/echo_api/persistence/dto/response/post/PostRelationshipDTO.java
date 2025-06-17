package com.example.echo_api.persistence.dto.response.post;

/**
 * Represents a standardised response format for post relationships.
 *
 * @param liked Indicates if the requesting user has liked the requested post.
 */
// @formatter:off
public record PostRelationshipDTO(
    boolean liked,
    boolean reposted
) {}
// @formatter:on
