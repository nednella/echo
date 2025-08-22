package com.example.echo_api.integration.util;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

/**
 * Intercepts HTTP requests and sets the {@link HttpHeaders.AUTHORIZATION}
 * header with the stored {@code token}, if that token is present.
 */
// @formatter:off
@Component
public class AuthorizationHeaderInterceptor implements ClientHttpRequestInterceptor {

    private boolean bypassInterceptor = false;

    private String token;

    @Override
    public ClientHttpResponse intercept(
        HttpRequest request,
        byte[] body,
        ClientHttpRequestExecution execution
    ) throws IOException {
        if (bypassInterceptor) {
            return execution.execute(request, body);
        }

        if (token != null) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }

        return execution.execute(request, body);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void enable() {
        bypassInterceptor = false;
    }

    public void disable() {
        bypassInterceptor = true;
    }

} // @formatter:on
