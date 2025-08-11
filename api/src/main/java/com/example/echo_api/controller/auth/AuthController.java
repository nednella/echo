package com.example.echo_api.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.service.auth.onboarding.OnboardingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final OnboardingService onboardingService;

    @PostMapping(ApiConfig.Auth.ONBOARDING)
    public ResponseEntity<Void> onboarding() {
        onboardingService.onboard();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
