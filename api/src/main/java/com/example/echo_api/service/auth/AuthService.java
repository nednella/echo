package com.example.echo_api.service.auth;

import org.springframework.security.core.AuthenticationException;

import com.example.echo_api.exception.custom.badrequest.UsernameAlreadyExistsException;
import com.example.echo_api.persistence.dto.request.auth.LoginDTO;
import com.example.echo_api.persistence.dto.request.auth.SignupDTO;

public interface AuthService {

    /**
     * Authenticates the supplied credentials.
     * 
     * @param login The login request containing the credentials.
     * @throws AuthenticationException If the authentication fails.
     */
    public void login(LoginDTO login) throws AuthenticationException;

    /**
     * Registes and authenticates the supplied credentials.
     * 
     * @param signup The signup request containing the credentials.
     * @throws UsernameAlreadyExistsException If the supplied username is already
     *                                        taken.
     * @throws AuthenticationException        If the authentication fails.
     */
    public void signup(SignupDTO signup) throws UsernameAlreadyExistsException, AuthenticationException;

}
