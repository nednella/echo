package com.example.echo_api.service.webhook;

import org.springframework.stereotype.Service;

import com.clerk.backend_api.Clerk;
import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.persistence.dto.request.webhook.WebhookEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for acting upon received asynchronous {@link Clerk}
 * webhook notifications to ensure database synchronisation.
 */
@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

    private final ObjectMapper mapper;

    @Override
    public WebhookEvent deserializePayload(String payload) {
        try {
            return mapper.readValue(payload, WebhookEvent.class);
        } catch (Exception ex) {
            throw new DeserializationException(ex.getMessage());
        }
    }

}
