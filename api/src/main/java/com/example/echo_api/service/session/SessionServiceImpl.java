package com.example.echo_api.service.session;

import java.util.Map;
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
        return UUID.fromString(token.getClaim(ClerkConfig.ECHO_ID));
    }

    @Override
    public String getAuthenticatedUserClerkId() {
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return token.getSubject();
    }

    @Override
    public boolean isAuthenticatedUserOnboardingComplete() {
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> metadata = token.getClaimAsMap(ClerkConfig.METADATA);

        return Boolean.TRUE.equals(metadata.get(ClerkConfig.ONBOARDING_COMPLETE_KEY));
    }

}
