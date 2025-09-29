package com.example.echo_api.exception.handler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.testing.support.AbstractIntegrationTest;

class FrameworkExceptionHandlerIT extends AbstractIntegrationTest {

    @Test
    void handleMethodArgumentTypeMismatchException_Returns400BadRequest_WhenArgumentCannotBeCastedToExpectedType() {
        String path = ApiRoutes.PROFILE.FOLLOWERS;
        String notValidUUID = "345345345345";

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Request path parameter 'id' must be a valid 'UUID'",
            null);

        authenticatedClient.get()
            .uri(path, notValidUUID)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void handleHttpMessageNotReadableException_Returns400BadRequest_WhenRequiredRequestBodyIsMissing() {
        String path = ApiRoutes.PROFILE.ME;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Required request body is missing",
            null);

        authenticatedClient.put()
            .uri(path)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void handleHttpMessageNotReadableException_Returns400BadRequest_WhenRequiredRequestBodyIsMalformed() {
        String path = ApiRoutes.PROFILE.ME;
        String malformedJson = """
            {"name":,
            """;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Required request body is malformed",
            null);

        authenticatedClient.put()
            .uri(path)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(malformedJson)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void handleNoResourceFoundException_Returns404ResourceNotFound_WhenPathIsNotMappedToAnyController() {
        String path = "/invalid_path";

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            "Resource not found",
            null);

        authenticatedClient.get()
            .uri(path)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void handleHttpRequestMethodNotSupportedException_Returns405MethodNotAllowedWithAllowHeader_WhenUnsupportedHttpMethod() {
        String path = ApiRoutes.PROFILE.ME;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.METHOD_NOT_ALLOWED,
            "Unsupported HTTP Method: PATCH",
            null);

        authenticatedClient.patch()
            .uri(path)
            .exchange()
            .expectStatus().isEqualTo(405)
            .expectHeader().value("Allow", v -> { // Path /profile/me should support GET and PUT
                assertThat(v).contains("GET");
                assertThat(v).contains("PUT");
            })
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void handleHttpMediaTypeNotSupportedException_Returns415UnsupportedMediaType_WhenUnsupportedMediaType() {
        String path = ApiRoutes.PROFILE.ME;
        var body = "some body";

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            "Unsupported Content-Type: text/plain;charset=UTF-8",
            null);

        authenticatedClient.put()
            .uri(path)
            .bodyValue(body)
            .exchange()
            .expectStatus().isEqualTo(415)
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

}
