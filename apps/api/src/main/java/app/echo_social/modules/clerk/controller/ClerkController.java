package app.echo_social.modules.clerk.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import app.echo_social.modules.clerk.api.ClerkAPI;
import app.echo_social.modules.clerk.service.ClerkOnboardingService;
import app.echo_social.modules.clerk.service.ClerkWebhookService;
import app.echo_social.modules.user.entity.User;
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
