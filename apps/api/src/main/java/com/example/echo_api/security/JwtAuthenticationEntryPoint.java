package com.example.echo_api.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom {@link AuthenticationEntryPoint}, which should be called by
 * {@link ExceptionTranslationFilter} in cases of missing user authentication
 * when requesting a protected endpoint.
 * 
 * <p>
 * For more information, refer to:
 * <ul>
 * <li>https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html#page-title
 * <li>https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-exceptiontranslationfilter
 * </ul>
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    // @formatter:off
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
        throws IOException, ServletException
    {
        resolver.resolveException(request, response, null, ex);
    }
    // @formatter:on

}
