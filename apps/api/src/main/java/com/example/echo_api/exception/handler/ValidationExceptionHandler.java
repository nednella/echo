package com.example.echo_api.exception.handler;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.exception.ErrorResponseFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Order(2)
@Slf4j
@ControllerAdvice
public class ValidationExceptionHandler {

    /**
     * Handle thrown {@link ConstraintViolationException} by converting into an
     * {@link ErrorResponse} with status {@link HttpStatus.BAD_REQUEST} and a
     * message indicating what caused the request validation to fail.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> handleConstraintViolationException(
        HttpServletRequest request,
        ConstraintViolationException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        String raw = ex.getMessage();
        String message = raw.substring(raw.indexOf(":") + 2);

        return ErrorResponseFactory.build(
            request,
            HttpStatus.BAD_REQUEST,
            message);
    }

    /**
     * Handle thrown {@link MethodArgumentNotValidException} by converting into an
     * {@link ErrorResponse} with status {@link HttpStatus.BAD_REQUEST} and a
     * message indicating what caused the request validation to fail.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        HttpServletRequest request,
        MethodArgumentNotValidException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        // parse validation message from exception
        String message = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();

        return ErrorResponseFactory.build(
            request,
            HttpStatus.BAD_REQUEST,
            message);
    }

}
