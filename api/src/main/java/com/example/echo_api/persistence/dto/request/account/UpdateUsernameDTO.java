package com.example.echo_api.persistence.dto.request.account;

import com.example.echo_api.validation.account.annotations.Username;
import com.example.echo_api.validation.sequence.Advanced;
import com.example.echo_api.validation.sequence.Basic;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a request to update the username of the authenticated account.
 * 
 * @param username The new username for the account. Required field. Must match
 *                 the format specified by {@link Username}.
 */
// @formatter:off
public record UpdateUsernameDTO(

    @NotNull(message = "Username is required.", groups = Basic.class)
    @Username(groups = Advanced.class)
    String username

) {}
// @formatter:on