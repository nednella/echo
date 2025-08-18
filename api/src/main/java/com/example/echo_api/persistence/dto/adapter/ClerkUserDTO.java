package com.example.echo_api.persistence.dto.adapter;

import java.util.Map;

import org.springframework.lang.NonNull;

/**
 * Internal DTO used to represent a Clerk user object returned by the Clerk SDK.
 * 
 * <p>
 * Abstracts away the optional/nullable Clerk User object design and prevents
 * namespace clashing.
 * 
 * @param id             the unique identifier of the Clerk user (non-null)
 * @param username       the unique username of the Clerk user (non-null)
 * @param externalId     the identifier of the corresponding user in the local
 *                       application, if synchronised (nullable)
 * @param imageUrl       the URL of the user's profile image (nullable)
 * @param publicMetadata the public metadata associated with the user,
 *                       represented as a key-value map (non-null, but may be
 *                       empty)
 */
public record ClerkUserDTO(
    @NonNull String id,
    @NonNull String username,
    String externalId,
    String imageUrl,
    @NonNull Map<String, Object> publicMetadata
) {}
