package com.example.echo_api.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom {@link AccessDeniedHandler}, which should be called by
 * {@link ExceptionTranslationFilter} in cases of an authentication object
 * missing the required permissions to access the requested endpoint.
 * 
 * <p>
 * For more information, refer to:
 * <ul>
 * <li>https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-exceptiontranslationfilter
 * </ul>
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver resolver;

    public JwtAccessDeniedHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    // @formatter:off
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
            throws IOException, ServletException
    {
        resolver.resolveException(request, response, null, ex);
    }
    // @formatter:on

}
