package com.example.echo_api.controller.webhook;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.service.clerk.webhook.ClerkWebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final ClerkWebhookService clerkWebhookService;

    @PostMapping(ApiConfig.Webhook.CLERK_EVENT)
    public ResponseEntity<Void> clerkEvent(@RequestHeader HttpHeaders headers, @RequestBody String payload) {
        clerkWebhookService.verify(headers, payload);
        var event = clerkWebhookService.deserializePayload(payload);
        clerkWebhookService.handleClerkWebhookEvent(event);
        return ResponseEntity.noContent().build();
    }

}
