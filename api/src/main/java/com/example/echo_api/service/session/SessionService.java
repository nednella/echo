package com.example.echo_api.service.session;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.service.account.AccountService;

public interface SessionService {

    /**
     * Retrieves the authenticated {@link Account} entity from
     * {@link SecurityContextHolder}.
     *
     * @return The authenticated {@link Account}, or {@code null} if no user is
     *         authenticated.
     */
    public Account getAuthenticatedUser();

    /**
     * Custom authentication method, required when manually authenticating users
     * through a custom {@code /login} API endpoint.
     * 
     * <p>
     * The method attempts to authenticate the supplied credentials against the
     * database using {@link AuthenticationManager}, and stores the authenticated
     * {@link SecurityContext} in the HTTP session if successful.
     * 
     * @param username The username of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @throws AuthenticationException if authentication fails.
     */
    public void authenticate(String username, String password) throws AuthenticationException;

    /**
     * Reauthentication method, required when there is a change to the authenticated
     * {@link Account} information via {@link AccountService}.
     * 
     * <p>
     * The method reauthenticates the specified user by creating a new authenticated
     * token and updating {@link SecurityContext} in both the application and the
     * HTTP session.
     * 
     * @param user The {@link Account} to reauthenticate.
     */
    public void reauthenticate(Account user);

}
