package com.example.echo_api.controller.clerk;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.service.clerk.sync.ClerkSyncService;
import com.example.echo_api.service.clerk.webhook.ClerkWebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ClerkController {

    private final ClerkSyncService clerkSyncService;
    private final ClerkWebhookService clerkWebhookService;

    @PostMapping(ApiRoutes.CLERK.ONBOARDING)
    public ResponseEntity<User> clerkOnboarding() {
        User user = clerkSyncService.onboardAuthenticatedUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping(ApiRoutes.CLERK.WEBHOOK)
    public ResponseEntity<Void> clerkEvent(@RequestHeader HttpHeaders headers, @RequestBody String payload) {
        clerkWebhookService.verify(headers, payload);
        clerkWebhookService.handleWebhook(payload);
        return ResponseEntity.noContent().build();
    }

}
