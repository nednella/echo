package com.example.echo_api.persistence.dto.request.account;

import com.example.echo_api.validation.account.annotations.ConfirmationPasswordMatch;
import com.example.echo_api.validation.account.annotations.NewPasswordUnique;
import com.example.echo_api.validation.account.annotations.Password;
import com.example.echo_api.validation.sequence.Advanced;
import com.example.echo_api.validation.sequence.Basic;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a request to update the password of the authenticated account.
 * 
 * @param currentPassword      The current password of the account. Required
 *                             field. Must not match {@code newPassword}.
 * @param newPassword          The new password for the account. Required field.
 *                             Must not match {@code currentPassword}.
 * @param confirmationPassword The confirmation password for the account.
 *                             Required field. Must match {@code newPassword}.
 */
// @formatter:off
@NewPasswordUnique(groups = Advanced.class)
@ConfirmationPasswordMatch(groups = Advanced.class)
public record UpdatePasswordDTO(

    @NotNull(message = "Current password is required.", groups = Basic.class)
    @JsonProperty("current_password")
    String currentPassword,

    @NotNull(message = "New password is required.", groups = Basic.class)
    @Password(groups = Advanced.class)
    @JsonProperty("new_password")
    String newPassword,

    @NotNull(message = "Confirmation password is required.", groups = Basic.class)
    @JsonProperty("confirmation_password")
    String confirmationPassword

) {}
// @formatter:on
