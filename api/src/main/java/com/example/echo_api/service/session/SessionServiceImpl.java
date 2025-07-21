package com.example.echo_api.service.session;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.echo_api.persistence.model.account.SecurityAccount;
import com.example.echo_api.persistence.model.account.Account;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing the session and authentication state of a
 * {@link Account}.
 * 
 * @see AuthenticationManager
 * @see SecurityContextRepository
 * @see SecurityContextHolderStrategy
 */
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy();

    @Override
    public Account getAuthenticatedUser() {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return ((SecurityAccount) authentication.getPrincipal()).getAccount();
    }

    @Override
    public void authenticate(String username, String password) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
            .unauthenticated(username, password);

        Authentication authenticatedToken = authenticationManager.authenticate(token);

        saveAuthenticationToSession(authenticatedToken);
    }

    @Override
    public void reauthenticate(Account user) {
        SecurityAccount auth = new SecurityAccount(user);

        UsernamePasswordAuthenticationToken authenticatedToken = UsernamePasswordAuthenticationToken
            .authenticated(auth, null, auth.getAuthorities());

        saveAuthenticationToSession(authenticatedToken);
    }

    /**
     * Internal method for saving an authenticated token to the HTTP session using
     * {@link SecurityContextRepository}. This is the default Spring
     * {@link UsernamePasswordAuthenticationFilter} behaviour.
     * 
     * <p>
     * For more information, refer to:
     * <ul>
     * <li>Manual authentication:
     * {@link https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html#store-authentication-manually}.
     * <li>CreateEmptyContext:
     * {@link https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-securitycontextholder}
     * <li>SecurityContextHolderStrategy:
     * {@link https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html#use-securitycontextholderstrategy}
     * </ul>
     * 
     * @param token The authenticated token to store in the session.
     */
    private void saveAuthenticationToSession(Authentication token) {
        // set authenticated token in a fresh security context
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(token);
        securityContextHolderStrategy.setContext(context);

        // retrieve current http request/response
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
            .currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
            .currentRequestAttributes()).getResponse();

        // persist the security context within the HTTP session
        securityContextRepository.saveContext(context, request, response);
    }

}
