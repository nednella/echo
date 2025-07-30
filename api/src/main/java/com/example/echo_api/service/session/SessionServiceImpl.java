package com.example.echo_api.service.session;

import org.springframework.stereotype.Service;

import com.example.echo_api.persistence.model.account.Account;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    @Override
    public Account getAuthenticatedUser() {
        throw new UnsupportedOperationException();
    }

}
