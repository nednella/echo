package app.echo_social.modules.clerk.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.clerk.backend_api.Clerk;
import app.echo_social.exception.ApplicationException;
import app.echo_social.modules.clerk.dto.webhook.ClerkWebhook;
import app.echo_social.modules.clerk.dto.webhook.UserDelete;
import app.echo_social.modules.clerk.dto.webhook.UserUpsert;
import app.echo_social.modules.clerk.exception.ClerkErrorCode;
import app.echo_social.modules.clerk.mapper.ClerkUserMapper;
import app.echo_social.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svix.Webhook;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for verifying and deserializing received asynchronous
 * {@link Clerk} webhook notifications.
 */
@Service
@RequiredArgsConstructor
class ClerkWebhookServiceImpl implements ClerkWebhookService {

    private final ClerkSyncService syncService;

    private final Webhook svixWebhook;
    private final ObjectMapper mapper;

    @Override
    public void verify(HttpHeaders headers, String payload) {
        try {
            svixWebhook.verify(payload, Utils.convertHeaders(headers));
        } catch (Exception ex) {
            throw ClerkErrorCode.WEBHOOK_SIGNATURE_INVALID.buildAsException();
        }
    }

    @Override
    public void handleWebhook(String payload) {
        var event = deserialize(payload);
        switch (event.type()) {
            case USER_CREATED, USER_UPDATED -> handleUserUpsert((UserUpsert) event.data());
            case USER_DELETED -> handleUserDelete((UserDelete) event.data());
        }
    }

    /**
     * Deserializes a webhook JSON payload string into an appropriate
     * {@link ClerkWebhook} based on its type information.
     * 
     * @param payload The JSON payload string to deserialize
     * @return The mapped {@link ClerkWebhook}
     * @throws ApplicationException If there was an issue when deserializing the
     *                              JSON payload for whatever reason
     */
    ClerkWebhook deserialize(String payload) {
        try {
            return mapper.readValue(payload, ClerkWebhook.class);
        } catch (Exception ex) {
            throw ClerkErrorCode.WEBHOOK_PAYLOAD_INVALID.buildAsException(ex.getMessage());
        }
    }

    private void handleUserUpsert(UserUpsert data) {
        syncService.ingestUserUpserted(ClerkUserMapper.fromWebhook(data));
    }

    private void handleUserDelete(UserDelete data) {
        syncService.ingestUserDeleted(data.id());
    }

}
