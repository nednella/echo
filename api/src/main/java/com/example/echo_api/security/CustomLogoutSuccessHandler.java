package com.example.echo_api.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom {@link LogoutSuccessHandler} class to be consumed as part of the
 * Spring Security default {@code /logout} process.
 * 
 * <p>
 * The class ensures that the logout endpoint always returns a 204 status code
 * instead of a 4xx status code.
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
