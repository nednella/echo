package com.example.echo_api.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.echo_api.exception.custom.unauthorised.UnauthorisedRequestException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom {@link OncePerRequestFilter} class to be consumed as part of the
 * Spring Security filter chain.
 * 
 * <p>
 * The filter is responsible for catching exceptions thrown during the filter
 * chain process, and delegating their resolution to the
 * {@link HandlerExceptionResolver}, for consistent handling via the
 * {@link GlobalControllerAdvice}.
 * 
 */
@Slf4j
@Component
public class FilterChainExceptionHandler extends OncePerRequestFilter {

    private HandlerExceptionResolver resolver;

    public FilterChainExceptionHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.debug("Handling Spring Security filter chain exception: {}", ex.getMessage());

            /**
             * Replace any FORBIDDEN exception with UNAUTHORISED exception for
             * unauthenticated users.
             * 
             * NOTE: Spring Security creates anonymous authentication objects for
             * unauthenticated users. Those users may encounter an
             * AuthorizationDeniedException when requesting protected endpoints on the
             * grounds of not having the required permissions (status code 403).
             * 
             * The more suitable exception would be a {@code UnauthorisedException} due to
             * the unauthorised request (status code 401).
             * 
             * For more information, refer to:
             * https://docs.spring.io/spring-security/reference/servlet/authentication/anonymous.html
             * 
             */
            if (ex instanceof AuthorizationDeniedException) {
                ex = new UnauthorisedRequestException();
            }

            /** Delegate filter chain exceptions to the global controller advice handlers */
            resolver.resolveException(request, response, null, ex);
        }
    }
}
