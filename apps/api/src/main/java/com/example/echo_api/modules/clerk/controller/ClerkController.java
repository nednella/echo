package com.example.echo_api.modules.clerk.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.clerk.service.ClerkOnboardingService;
import com.example.echo_api.modules.clerk.service.ClerkWebhookService;
import com.example.echo_api.modules.user.entity.User;
import com.example.echo_api.shared.constant.ApiRoutes;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Clerk API")
@RestController
@RequiredArgsConstructor
public class ClerkController {

    private final ClerkOnboardingService onboardingService;
    private final ClerkWebhookService webhookService;

    @PostMapping(ApiRoutes.CLERK.ONBOARDING)
    public ResponseEntity<User> clerkOnboarding() {
        User user = onboardingService.onboardAuthenticatedUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Hidden
    @PostMapping(ApiRoutes.CLERK.WEBHOOK)
    public ResponseEntity<Void> clerkEvent(@RequestHeader HttpHeaders headers, @RequestBody String payload) {
        webhookService.verify(headers, payload);
        webhookService.handleWebhook(payload);
        return ResponseEntity.noContent().build();
    }

}
