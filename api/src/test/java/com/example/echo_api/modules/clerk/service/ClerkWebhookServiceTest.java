package com.example.echo_api.modules.clerk.service;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.clerk.dto.ClerkUser;
import com.example.echo_api.modules.clerk.exception.ClerkErrorCode;
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

  @InjectMocks
  private ClerkWebhookServiceImpl clerkWebhookService;

  private ObjectMapper mapper = new ObjectMapper();

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
  void verify_ThrowsWhenWebhookSignatureInvalid() throws com.svix.exceptions.WebhookVerificationException {
    // arrange
    ClerkErrorCode errorCode = ClerkErrorCode.WEBHOOK_SIGNATURE_INVALID;

    HttpHeaders headers = HttpHeaders.EMPTY;
    String payload = "a_string_placeholder_for_webhook_payload";

    // Checked exception, must be declared as throws on test method declaration
    doThrow(new com.svix.exceptions.WebhookVerificationException(""))
      .when(svixWebhook).verify(payload, Utils.convertHeaders(headers));

    // act & assert
    var ex = assertThrows(ApplicationException.class, () -> clerkWebhookService.verify(headers, payload));
    assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage());
  }

  @Test
  void handleWebhook_DeserializesAndDispatchesUserCreatedEvent() {
    String payload = """
          {
            "data": {
              "id": "user_29wBMCtzATuFJut8jO2VNTVekS4",
              "username": "SomeUsername",
              "public_metadata": {}
            },
            "type": "user.created"
          }
      """;

    assertDoesNotThrow(() -> clerkWebhookService.handleWebhook(payload));
    verify(clerkSyncService).ingestUserUpserted(any(ClerkUser.class));
  }

  @Test
  void handleWebhook_DeserializesAndThrows_WhenClerkUserCreatedEventMissingId() {
    String payload = """
          {
            "data": {
              "username": "SomeUsername",
              "public_metadata": {}
            },
            "type": "user.created"
          }
      """;

    assertThrows(IllegalArgumentException.class, () -> clerkWebhookService.handleWebhook(payload));
    verify(clerkSyncService, never()).ingestUserUpserted(any(ClerkUser.class));
  }

  @Test
  void handleWebhook_DeserializesAndThrows_WhenClerkUserCreatedEventMissingUsername() {
    String payload = """
          {
            "data": {
              "id": "user_29wBMCtzATuFJut8jO2VNTVekS4",
              "public_metadata": {}
            },
            "type": "user.created"
          }
      """;

    assertThrows(IllegalArgumentException.class, () -> clerkWebhookService.handleWebhook(payload));
    verify(clerkSyncService, never()).ingestUserUpserted(any(ClerkUser.class));
  }

  @Test
  void handleWebhook_DeserializesAndThrows_WhenClerkUserCreatedEventMissingPublicMetadata() {
    String payload = """
          {
            "data": {
              "id": "user_29wBMCtzATuFJut8jO2VNTVekS4",
              "username": "SomeUsername"
            },
            "type": "user.created"
          }
      """;

    assertThrows(IllegalArgumentException.class, () -> clerkWebhookService.handleWebhook(payload));
    verify(clerkSyncService, never()).ingestUserUpserted(any(ClerkUser.class));
  }

  @Test
  void handleWebhook_DeserializesAndDispatchesUserUpdatedEvent() {
    String payload = """
          {
            "data": {
              "id": "user_29wBMCtzATuFJut8jO2VNTVekS4",
              "username": "SomeUsername",
              "public_metadata": {}
            },
            "type": "user.updated"
          }
      """;

    assertDoesNotThrow(() -> clerkWebhookService.handleWebhook(payload));
    verify(clerkSyncService).ingestUserUpserted(any(ClerkUser.class));
  }

  @Test
  void handleWebhook_DeserializesAndThrows_WhenClerkUserUpdatedEventMissingId() {
    String payload = """
          {
            "data": {
              "username": "SomeUsername",
              "public_metadata": {}
            },
            "type": "user.updated"
          }
      """;

    assertThrows(IllegalArgumentException.class, () -> clerkWebhookService.handleWebhook(payload));
    verify(clerkSyncService, never()).ingestUserUpserted(any(ClerkUser.class));
  }

  @Test
  void handleWebhook_DeserializesAndThrows_WhenClerkUserUpdatedEventMissingUsername() {
    String payload = """
          {
            "data": {
              "id": "user_29wBMCtzATuFJut8jO2VNTVekS4",
              "public_metadata": {}
            },
            "type": "user.updated"
          }
      """;

    assertThrows(IllegalArgumentException.class, () -> clerkWebhookService.handleWebhook(payload));
    verify(clerkSyncService, never()).ingestUserUpserted(any(ClerkUser.class));
  }

  @Test
  void handleWebhook_DeserializesAndThrows_WhenClerkUserUpdatedEventMissingPublicMetadata() {
    String payload = """
          {
            "data": {
              "id": "user_29wBMCtzATuFJut8jO2VNTVekS4",
              "username": "SomeUsername"
            },
            "type": "user.updated"
          }
      """;

    assertThrows(IllegalArgumentException.class, () -> clerkWebhookService.handleWebhook(payload));
    verify(clerkSyncService, never()).ingestUserUpserted(any(ClerkUser.class));
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
    verify(clerkSyncService).ingestUserDeleted(anyString());
  }

  @Test
  void handleWebhook_ThrowsWhenDeserializationFails() {
    String payload = "";

    assertThrows(ApplicationException.class, () -> clerkWebhookService.handleWebhook(payload));
  }

}
