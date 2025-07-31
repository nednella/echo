package com.example.echo_api.service.session;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.echo_api.exception.custom.badrequest.OnboardingIncompleteException;
import com.example.echo_api.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final UserRepository userRepository;

    @Override
    public UUID getAuthenticatedUserId() throws OnboardingIncompleteException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Jwt token = (Jwt) authentication.getPrincipal();
        String userId = token.getClaim("echo_id");

        try {
            return UUID.fromString(userId);
        } catch (Exception ex) {
            return userRepository.findIdByClerkId(token.getSubject())
                    .orElseThrow(OnboardingIncompleteException::new);
        }
    }

    @Override
    public String getAuthenticatedUserClerkId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt token = (Jwt) authentication.getPrincipal();

        return token.getSubject();
    }

}
