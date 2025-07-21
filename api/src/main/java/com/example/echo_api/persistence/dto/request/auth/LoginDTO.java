package com.example.echo_api.persistence.dto.request.auth;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a request to authenticate a account.
 * 
 * @param username The username of the account to authenticate. Required field.
 * @param password The password of the account to authenticate. Required field.
 */
// @formatter:off
public record LoginDTO(

    @NotNull(message = "Username is required.")
    String username,
    
    @NotNull(message = "Password is required.")
    String password

) {}
// @formatter:on
