package com.example.echo_api.controller.webhook;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.service.webhook.WebhookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping(ApiConfig.Webhook.CLERK_USER_EVENT)
    public void clerkUserUpdate() {

    }

}
