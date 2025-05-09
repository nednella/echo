package com.example.echo_api.service.account;

import java.util.UUID;

import com.example.echo_api.exception.custom.password.IncorrectCurrentPasswordException;
import com.example.echo_api.exception.custom.username.UsernameAlreadyExistsException;
import com.example.echo_api.persistence.model.account.Role;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;

public interface AccountService {

    /**
     * Check whether an {@link Account} exists by {@code id}.
     * 
     * @param id The id to check.
     * @return True if an account exists, else false.
     */
    public boolean existsById(UUID id);

    /**
     * Check whether an {@link Account} exists by {@code username}.
     * 
     * @param username The username to check.
     * @return True if an account exists, else false.
     */
    public boolean existsByUsername(String username);

    /**
     * Registers a new {@link Account} with the specified {@code username} and
     * {@code password}.
     * 
     * <p>
     * If the username is available, it creates an {@link Account} object, and
     * associated {@link Profile} object.
     * 
     * @param username The username of the account to register.
     * @param password The password of the account to register.
     * @return The newly registered {@link Account}.
     * @throws UsernameAlreadyExistsException If the username is already taken.
     */
    public Account register(String username, String password) throws UsernameAlreadyExistsException;

    /**
     * Registers a new {@link Account} with the specified {@code username},
     * {@code password} and {@code role}.
     * 
     * <p>
     * If the username is available, it creates an {@link Account} object, and
     * associated {@link Profile} object.
     * 
     * @param username The username of the account to register.
     * @param password The password of the account to register.
     * @param role     The role to assign to the new account.
     * @return The created {@link Account} object.
     * @throws UsernameAlreadyExistsException If the username is already taken.
     */
    public Account registerWithRole(String username, String password, Role role) throws UsernameAlreadyExistsException;

    /**
     * Updates the authenticated account username.
     * 
     * @param username The new username for the authenticated user.
     * @throws UsernameAlreadyExistsException If the username is already taken.
     */
    public void updateUsername(String username) throws UsernameAlreadyExistsException;

    /**
     * Update the authenticated account password.
     * 
     * @param currentPassword The current password of the authenticated accopunt.
     * @param newPassword     The new password for the authenticated account.
     * @throws IncorrectCurrentPasswordException If the supplied
     *                                           {@code currentPassword} does not
     *                                           match the authenticated account's
     *                                           existing password.
     */
    public void updatePassword(String currentPassword, String newPassword) throws IncorrectCurrentPasswordException;

}
