package com.example.echo_api.controller.webhook;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.persistence.dto.request.webhook.WebhookEvent;
import com.example.echo_api.service.webhook.WebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping(ApiConfig.Webhook.CLERK_USER_EVENT)
    public ResponseEntity<Void> clerkUserEvent(@RequestHeader HttpHeaders headers, @RequestBody String payload) {
        webhookService.verify(headers, payload);
        WebhookEvent event = webhookService.deserializePayload(payload);
        return ResponseEntity.noContent().build();
    }

}
