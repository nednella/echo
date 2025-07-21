package com.example.echo_api.integration.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class TestUtils {

    /**
     * Creates an {@link HttpEntity} with the specified body and sets the content
     * type to {@code application/json}.
     *
     * @param body the body of the request, which will be serialized to JSON
     * @param <T>  the type of the body
     * @return an {@link HttpEntity} containing the specified body and headers
     *         indicating the content type is JSON
     */
    public static <T> HttpEntity<T> createJsonRequestEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    /**
     * Asserts that the Set-Cookie header contains a cookie starting with the
     * specified prefix.
     *
     * @param response the ResponseEntity to check
     * @param prefix   the expected prefix of the Set-Cookie header
     */
    public static void assertSetCookieStartsWith(ResponseEntity<?> response, String prefix) {
        String setCookieHeader = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(setCookieHeader);
        assertTrue(setCookieHeader.startsWith(prefix), "Set-Cookie header does not start with " + prefix);
    }

}
