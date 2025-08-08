package com.example.echo_api.service.auth.session;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.echo_api.config.ClerkConfig;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for obtaining user information from the authentication
 * object for the authenticated user.
 */
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    @Override
    public UUID getAuthenticatedUserId() {
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UUID.fromString(token.getClaim(ClerkConfig.JWT_ECHO_ID_CLAIM));
    }

    @Override
    public String getAuthenticatedUserClerkId() {
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return token.getSubject();
    }

    @Override
    public boolean isAuthenticatedUserOnboardingComplete() {
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object onboarded = token.getClaim(ClerkConfig.JWT_ONBOARDED_CLAIM);

        return Boolean.TRUE.equals(onboarded);
    }

}
