package com.example.echo_api.modules.clerk.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.exception.custom.unauthorised.WebhookVerificationException;
import com.example.echo_api.modules.clerk.dto.webhook.ClerkWebhookEvent;
import com.example.echo_api.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svix.Webhook;

/**
 * Unit test class for {@link ClerkWebhookService}.
 */
@ExtendWith(MockitoExtension.class)
class ClerkWebhookServiceTest {

  @Mock
  private ClerkSyncService clerkSyncService;

  @Mock
  private Webhook svixWebhook;

  private ObjectMapper mapper = new ObjectMapper();

  @InjectMocks
  private ClerkWebhookServiceImpl clerkWebhookService;

  @BeforeEach
  void setUp() {
    // Manually instantiate with a real ObjectMapper
    clerkWebhookService = new ClerkWebhookServiceImpl(clerkSyncService, svixWebhook, mapper);
  }

  @Test
  void verify_VerifiesWebhook() {
    // arrange
    HttpHeaders headers = HttpHeaders.EMPTY;
    String payload = "a_string_placeholder_for_webhook_payload";

    // act & assert
    assertDoesNotThrow(() -> clerkWebhookService.verify(headers, payload));
  }

  @Test
  void verify_ThrowsWhenWebhookInvalid() throws com.svix.exceptions.WebhookVerificationException {
    // arrange
    HttpHeaders headers = HttpHeaders.EMPTY;
    String payload = "a_string_placeholder_for_webhook_payload";

    // Checked exception, must be declared as throws on test method declaration
    doThrow(new com.svix.exceptions.WebhookVerificationException(""))
      .when(svixWebhook).verify(payload, Utils.convertHeaders(headers));

    // act & assert
    assertThrows(WebhookVerificationException.class, () -> clerkWebhookService.verify(headers, payload));
  }

  @Test
  void handleWebhook_DeserializesAndDispatchesUserCreatedEvent() {
    String payload = """
          {
            "data": {
              "id": "user_29wBMCtzATuFJut8jO2VNTVekS4",
              "username": null
            },
            "type": "user.created"
          }
      """;

    assertDoesNotThrow(() -> clerkWebhookService.handleWebhook(payload));
    verify(clerkSyncService).handleWebhookEvent(any(ClerkWebhookEvent.class));
  }

  @Test
  void handleWebhook_DeserializesAndDispatchesUserUpdatedEvent() {
    String payload = """
          {
            "data": {
              "id": "user_29wBMCtzATuFJut8jO2VNTVekS4",
              "username": null
            },
            "type": "user.updated"
          }
      """;

    assertDoesNotThrow(() -> clerkWebhookService.handleWebhook(payload));
    verify(clerkSyncService).handleWebhookEvent(any(ClerkWebhookEvent.class));
  }

  @Test
  void handleWebhook_DeserializesAndDispatchesUserDeletedEvent() {
    String payload = """
          {
            "data": {
              "id": "user_29wBMCtzATuFJut8jO2VNTVekS4"
            },
            "type": "user.deleted"
          }
      """;

    assertDoesNotThrow(() -> clerkWebhookService.handleWebhook(payload));
    verify(clerkSyncService).handleWebhookEvent(any(ClerkWebhookEvent.class));
  }

  @Test
  void handleWebhook_ThrowsWhenDeserializationFails() {
    String payload = "";

    assertThrows(DeserializationException.class, () -> clerkWebhookService.handleWebhook(payload));
  }

}
