package com.example.echo_api.persistence.dto.request.auth;

import com.example.echo_api.validation.account.annotations.Password;
import com.example.echo_api.validation.account.annotations.Username;
import com.example.echo_api.validation.sequence.Advanced;
import com.example.echo_api.validation.sequence.Basic;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a request to register a account.
 * 
 * @param username The username of the account to register. Required field. Must
 *                 match the format specified by {@link Username}.
 * @param password The password of the account to register. Required field. Must
 *                 match the format specified by {@link Password}.
 */
// @formatter:off
public record SignupDTO(

    @NotNull(message = "Username is required.", groups = Basic.class)
    @Username(groups = Advanced.class)
    String username,
    
    @NotNull(message = "Password is required.", groups = Basic.class)
    @Password(groups = Advanced.class)
    String password
    
) {}
// @formatter:on
