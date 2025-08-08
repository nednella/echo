package com.example.echo_api.service.clerk.webhook;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.clerk.backend_api.Clerk;
import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.exception.custom.unauthorised.WebhookVerificationException;
import com.example.echo_api.persistence.dto.request.webhook.clerk.ClerkWebhookEvent;
import com.example.echo_api.persistence.dto.request.webhook.clerk.data.UserDeleted;
import com.example.echo_api.persistence.dto.request.webhook.clerk.data.UserUpdated;
import com.example.echo_api.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svix.Webhook;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for acting upon received asynchronous {@link Clerk}
 * webhook notifications to maintain local database synchronisation.
 */
@Service
@RequiredArgsConstructor
public class ClerkWebhookServiceImpl implements ClerkWebhookService {

    private final UserService userService;

    private final Webhook svixWebhook;
    private final ObjectMapper mapper;

    @Override
    public void verify(HttpHeaders headers, String payload) {
        try {
            svixWebhook.verify(payload, convertHeaders(headers));
        } catch (Exception ex) {
            throw new WebhookVerificationException();
        }
    }

    @Override
    public ClerkWebhookEvent deserializePayload(String payload) {
        try {
            return mapper.readValue(payload, ClerkWebhookEvent.class);
        } catch (Exception ex) {
            throw new DeserializationException(ex.getMessage());
        }
    }

    @Override
    public void handleClerkWebhookEvent(ClerkWebhookEvent event) {
        switch (event.type()) {
            case USER_UPDATED:
                userService.handleClerkUserUpdated((UserUpdated) event.data());
                break;
            case USER_DELETED:
                userService.handleClerkUserDeleted((UserDeleted) event.data());
                break;
        }
    }

    /**
     * Helper method to convert Spring HttpHeaders into Java HttpHeaders to satisfy
     * the webhook verification method.
     * 
     * @param headers {@link org.springframework.http.HttpHeaders}
     * @return {@link java.net.http.HttpHeaders}
     */
    private java.net.http.HttpHeaders convertHeaders(HttpHeaders headers) {
        return java.net.http.HttpHeaders.of(headers, (t, v) -> true);
    }

}
