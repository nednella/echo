package com.example.echo_api.service.session;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    @Override
    public void getAuthenticatedUser() {
        throw new UnsupportedOperationException();
    }

}
