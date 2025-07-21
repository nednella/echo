package com.example.echo_api.integration.util;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Intercepts HTTP requests and responses to manage session cookies for
 * authentication as part of the application integration testing suite.
 * 
 * <p>
 * If an authentication-related request is made as part of an integration test,
 * the interceptor will bypass modification as to prevent failing tests that
 * expect a fresh session cookie in the server response.
 */
// @formatter:off
@Component
public class SessionCookieInterceptor implements ClientHttpRequestInterceptor {

    private boolean bypassInterceptor = false;

    private String sessionCookie;

    @Override
    @SuppressWarnings("null")
    public ClientHttpResponse intercept(
        @NonNull HttpRequest request,
        @NonNull byte[] body,
        @NonNull ClientHttpRequestExecution execution
    ) throws IOException {

        if (bypassInterceptor) {
            return execution.execute(request, body);
        }

        // Append session cookie to the request if present
        if (sessionCookie != null) {
            request.getHeaders().add(HttpHeaders.COOKIE, sessionCookie);
        }

        // Execute
        ClientHttpResponse response = execution.execute(request, body);

        // Capture session cookie from the response if present
        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (cookie != null &&
            cookie.startsWith("ECHO_SESSION")
        ) {
            sessionCookie = cookie;
        }

        return response;
    }

    public void enable() {
        bypassInterceptor = false;
    }

    public void disable() {
        bypassInterceptor = true;
    }

}
// @formatter:on