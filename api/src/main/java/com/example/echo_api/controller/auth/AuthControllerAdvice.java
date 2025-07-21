package com.example.echo_api.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.AbstractControllerAdvice;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(assignableTypes = AuthController.class)
public class AuthControllerAdvice extends AbstractControllerAdvice {

    /**
     * Handles authentication exceptions related to Spring Security for cases when:
     * 
     * <ul>
     * <li>A user could not be found via their username
     * <li>An authentication attempt was rejected on the grounds of bad credentials
     * </ul>
     */
    @ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class })
    ResponseEntity<ErrorDTO> handleAuthenticationException(HttpServletRequest request, Exception ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        return createExceptionHandler(
            request,
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.USERNAME_OR_PASSWORD_INCORRECT,
            null);
    }

    /**
     * Handles authentication exceptions related to Spring Security for cases when:
     * 
     * <ul>
     * <li>A user fails authentication due to a bad account status (locked,
     * disabled, etc.)
     * </ul>
     */
    @ExceptionHandler({ AccountStatusException.class })
    ResponseEntity<ErrorDTO> handleAccountStatusException(HttpServletRequest request, Exception ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        return createExceptionHandler(
            request,
            HttpStatus.UNAUTHORIZED,
            ErrorMessageConfig.Unauthorised.ACCOUNT_STATUS,
            ex.getMessage());
    }

}