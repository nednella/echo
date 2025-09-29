package com.example.echo_api.exception.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.exception.ErrorResponseFactory;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {

    /**
     * Handle thrown {@link ApplicationException} by converting into an
     * {@link ErrorResponse} based on its own HTTP status and message.
     */
    @ExceptionHandler(ApplicationException.class)
    ResponseEntity<ErrorResponse> handleApplicationException(HttpServletRequest request, ApplicationException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        return ErrorResponseFactory.build(
            request,
            ex.getStatus(),
            ex.getMessage());
    }

    /**
     * Handle any uncaught {@link Exception} not explicitly mapped elsewhere by
     * converting into an {@link ErrorResponse} with status
     * {@link HttpStatus#INTERNAL_SERVER_ERROR}.
     */
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleUncaughtException(HttpServletRequest request, Exception ex) {
        log.debug("Handling uncaught exception: {}", ex.getMessage());

        return ErrorResponseFactory.build(
            request,
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage());
    }

}
