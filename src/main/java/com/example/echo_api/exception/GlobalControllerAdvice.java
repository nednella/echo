package com.example.echo_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.ApplicationException;
import com.example.echo_api.exception.custom.badrequest.BadRequestException;
import com.example.echo_api.exception.custom.conflict.ConflictException;
import com.example.echo_api.exception.custom.forbidden.ForbiddenException;
import com.example.echo_api.exception.custom.internalserver.InternalServerException;
import com.example.echo_api.exception.custom.notfound.NotFoundException;
import com.example.echo_api.exception.custom.unauthorised.UnauthorisedException;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;

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
     * Handles Jakarta {@link ConstraintViolationException} for validation errors.
     * 
     * <p>
     * With fail-fast enabled, only a singular validation failure will be passed to
     * the exception. Parse the validator message from the exception and return via
     * {@code details}.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorDTO> handleConstraintViolationException(HttpServletRequest request,
        ConstraintViolationException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        // parse validation message from exception
        String msg = ex.getMessage();

        // get substring of msg starting at first index of ":" + 2
        String details = msg.substring(msg.indexOf(":") + 2);

        return createExceptionHandler(
            request,
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            details);
    }

    /**
     * Handles Jakarta {@link MethodArgumentNotValidException} for validation
     * errors.
     * 
     * <p>
     * With fail-fast enabled, only a singular validation failure will be passed to
     * the exception. Parse the validator message from the exception and return via
     * {@code details}.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(HttpServletRequest request,
        MethodArgumentNotValidException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        // parse validation message from exception
        String msg = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();

        return createExceptionHandler(
            request,
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            msg);
    }

    /**
     * Handles custom {@link ApplicationException} subclasses for HTTP errors.
     */
    @ExceptionHandler({
            BadRequestException.class,
            UnauthorisedException.class,
            ForbiddenException.class,
            NotFoundException.class,
            ConflictException.class,
            InternalServerException.class
    })
    ResponseEntity<ErrorDTO> handleApplicationException(HttpServletRequest request, ApplicationException ex) {
        log.debug("Handling exception: {}", ex.getMessage());
        return createExceptionHandler(
            request,
            ex.getHttpStatus(),
            ex.getMessage(),
            ex.getDetails());
    }

    /**
     * Handles any uncaught exceptions as a 500 Internal Server Error.
     */
    @ExceptionHandler({ Exception.class })
    ResponseEntity<ErrorDTO> handleGenericException(HttpServletRequest request, Exception ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        return createExceptionHandler(request,
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessageConfig.InternalServerError.INTERNAL_SERVER_ERROR,
            ex.getMessage());
    }

}
