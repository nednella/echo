package com.example.echo_api.exception.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.exception.ErrorResponseFactory;
import com.example.echo_api.security.ClerkOnboardingFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@ControllerAdvice
public class AuthenticationExceptionHandler {

    /**
     * Handles {@link AuthenticationException} subclass
     * {@link InsufficientAuthenticationException}, for cases when an authentication
     * is rejected because the credentials are not sufficiently trusted.
     * 
     * <p>
     * For more information, refer to:
     * <ul>
     * <li>https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/authentication/InsufficientAuthenticationException.html
     * </ul>
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    ResponseEntity<ErrorResponse> handleInsufficientAuthenticationException(
        HttpServletRequest request,
        InsufficientAuthenticationException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        String message = "Authentication token required";

        return ErrorResponseFactory.build(
            request,
            HttpStatus.UNAUTHORIZED,
            message);
    }

    /**
     * Handles {@link AuthenticationException} subclass
     * {@link OAuth2AuthenticationException}.
     * 
     * <p>
     * For more information, refer to:
     * <ul>
     * <li>https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/oauth2/core/OAuth2AuthenticationException.html
     * </ul>
     */
    @ExceptionHandler(OAuth2AuthenticationException.class)
    ResponseEntity<ErrorResponse> handleOAuth2AuthenticationException(
        HttpServletRequest request,
        OAuth2AuthenticationException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        return ErrorResponseFactory.build(
            request,
            HttpStatus.UNAUTHORIZED,
            ex.getMessage());
    }

    /**
     * Handles {@link OAuth2AuthenticationException} subclass
     * {@link InvalidBearerTokenException}, for cases when the provided bearer token
     * is invalid. Prevents revealing sensitive token-related information.
     * 
     * <p>
     * For more information, refer to:
     * <ul>
     * <li>https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/oauth2/core/OAuth2AuthenticationException.html
     * <li>https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/oauth2/server/resource/InvalidBearerTokenException.html
     * </ul>
     */
    @ExceptionHandler(InvalidBearerTokenException.class)
    ResponseEntity<ErrorResponse> handleInvalidBearerTokenException(
        HttpServletRequest request,
        InvalidBearerTokenException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        String message = "Invalid authentication token";

        return ErrorResponseFactory.build(
            request,
            HttpStatus.UNAUTHORIZED,
            message);
    }

    /**
     * Handles {@link AccessDeniedException} for cases when the authenticated
     * request is missing permissions required for the requested resource.
     * 
     * <p>
     * For more information, refer to:
     * <ul>
     * <li>https://docs.spring.io/spring-security/site/docs/4.1.0.RC2/apidocs/org/springframework/security/access/AccessDeniedException.html
     * </ul>
     * 
     * @see ClerkOnboardingFilter
     */
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorResponse> handleAccessDeniedException(
        HttpServletRequest request,
        AccessDeniedException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        return ErrorResponseFactory.build(
            request,
            HttpStatus.FORBIDDEN,
            ex.getMessage());
    }

}
