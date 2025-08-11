package com.example.echo_api.controller.clerk;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.service.auth.onboarding.OnboardingService;
import com.example.echo_api.service.clerk.webhook.ClerkWebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ClerkController {

    private final OnboardingService onboardingService;
    private final ClerkWebhookService clerkWebhookService;

    @PostMapping(ApiConfig.Clerk.ONBOARDING)
    public ResponseEntity<Void> clerkOnboarding() {
        onboardingService.onboard();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(ApiConfig.Clerk.WEBHOOK)
    public ResponseEntity<Void> clerkEvent(@RequestHeader HttpHeaders headers, @RequestBody String payload) {
        clerkWebhookService.verify(headers, payload);
        clerkWebhookService.handleWebhook(payload);
        return ResponseEntity.noContent().build();
    }

}
