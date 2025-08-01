package com.example.echo_api.service.session;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    @Override
    public UUID getAuthenticatedUserId() {
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UUID.fromString(token.getClaim("echo_id"));
    }

    @Override
    public String getAuthenticatedUserClerkId() {
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return token.getSubject();
    }

}
