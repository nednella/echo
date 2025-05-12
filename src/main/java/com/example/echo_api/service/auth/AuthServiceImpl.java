package com.example.echo_api.service.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.badrequest.UsernameAlreadyExistsException;
import com.example.echo_api.persistence.dto.request.auth.LoginDTO;
import com.example.echo_api.persistence.dto.request.auth.SignupDTO;
import com.example.echo_api.service.account.AccountService;
import com.example.echo_api.service.session.SessionService;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing {@link Account} creation and
 * authentication.
 * 
 * @see AccountService
 * @see SessionService
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountService accountService;
    private final SessionService sessionService;

    @Override
    public void login(LoginDTO login) throws AuthenticationException {
        sessionService.authenticate(login.username(), login.password());
    }

    @Override
    public void signup(SignupDTO signup) throws UsernameAlreadyExistsException, AuthenticationException {
        accountService.register(signup.username(), signup.password());
        sessionService.authenticate(signup.username(), signup.password());
    }

}
