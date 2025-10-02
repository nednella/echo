package com.example.echo_api.modules.clerk.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.clerk.api.ClerkAPI;
import com.example.echo_api.modules.clerk.service.ClerkOnboardingService;
import com.example.echo_api.modules.clerk.service.ClerkWebhookService;
import com.example.echo_api.modules.user.entity.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class ClerkController implements ClerkAPI {

    private final ClerkOnboardingService clerkOnboardingService;
    private final ClerkWebhookService clerkWebhookService;

    @Override
    public ResponseEntity<User> clerkOnboarding() {
        User user = clerkOnboardingService.onboardAuthenticatedUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Override
    public ResponseEntity<Void> clerkEvent(HttpHeaders headers, String payload) {
        clerkWebhookService.verify(headers, payload);
        clerkWebhookService.handleWebhook(payload);
        return ResponseEntity.noContent().build();
    }

}
