package com.example.echo_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.password.PasswordException;
import com.example.echo_api.exception.custom.username.UsernameException;
import com.example.echo_api.persistence.dto.response.error.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for the application, using {@link ControllerAdvice}
 * to handle exceptions thrown during the request processing lifecycle.
 * 
 * <p>
 * The methods in the class handle global occurrences for common HTTP status
 * codes (401, 403, 404, 500) across all controllers.
 * 
 * <p>
 * Methods also include Jakarta validation exception handlers.
 * 
 * <p>
 * Methods also include {@link UsernameException} and {@link PasswordException}
 * abstract class handlers.
 */
@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice extends AbstractControllerAdvice {

    /**
     * Jakarta Validation Exception
     * 
     * <p>
     * With fail-fast enabled, only a singular validation failure will be passed to
     * the exception. Parse the validator message from the exception and return via
     * {@code details}.
     */
    @ExceptionHandler({ ConstraintViolationException.class, MethodArgumentNotValidException.class })
    ResponseEntity<ErrorResponse> handleInvalidRequestException(HttpServletRequest request, Exception ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        // parse validation message from exception
        String msg = ex.getMessage();

        // get substring of msg starting at first index of ":" + 2
        String details = msg.substring(msg.indexOf(":") + 2);

        return createExceptionHandler(
            request,
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.INVALID_REQUEST,
            details);
    }

    /* Username/Password Exception */
    @ExceptionHandler({ UsernameException.class, PasswordException.class })
    ResponseEntity<ErrorResponse> handleUsernamePasswordException(HttpServletRequest request, Exception ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        return createExceptionHandler(
            request,
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            null);
    }

    /* 401 */
    @ExceptionHandler({ InsufficientAuthenticationException.class })
    ResponseEntity<ErrorResponse> handleInsufficientAuthenticationException(HttpServletRequest request, Exception ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        return createExceptionHandler(
            request,
            HttpStatus.UNAUTHORIZED,
            ErrorMessageConfig.UNAUTHORISED,
            null);
    }

    /* 403 */
    @ExceptionHandler({ AccessDeniedException.class })
    ResponseEntity<ErrorResponse> handleAccessDeniedException(HttpServletRequest request, Exception ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        return createExceptionHandler(
            request,
            HttpStatus.FORBIDDEN,
            ErrorMessageConfig.FORBIDDEN,
            null);
    }

    /* 404 */
    @ExceptionHandler({ NoResourceFoundException.class })
    ResponseEntity<ErrorResponse> handleNotFoundException(HttpServletRequest request, Exception ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        return createExceptionHandler(
            request,
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NOT_FOUND,
            null);
    }

    /* 500 */
    @ExceptionHandler({ Exception.class })
    ResponseEntity<ErrorResponse> handleGenericException(HttpServletRequest request, Exception ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        return createExceptionHandler(request,
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessageConfig.INTERNAL_SERVER_ERROR,
            ex.getMessage());
    }

}
