package com.example.echo_api.persistence.dto.request.auth;

import com.example.echo_api.validation.account.annotations.Password;
import com.example.echo_api.validation.account.annotations.Username;
import com.example.echo_api.validation.sequence.AdvancedCheck;
import com.example.echo_api.validation.sequence.BasicCheck;

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

    @NotNull(message = "Username is required.", groups = BasicCheck.class)
    @Username(groups = AdvancedCheck.class)
    String username,
    
    @NotNull(message = "Password is required.", groups = BasicCheck.class)
    @Password(groups = AdvancedCheck.class)
    String password
    
) {}
// @formatter:on
