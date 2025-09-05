package com.example.echo_api.exception.handler;

import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.exception.ErrorResponseFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Order(1)
@Slf4j
@ControllerAdvice
public class FrameworkExceptionHandler {

    /**
     * Handle thrown {@link MethodArgumentTypeMismatchException} by converting into
     * an {@link ErrorResponse} with status {@link HttpStatus.BAD_REQUEST}.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
        HttpServletRequest request,
        MethodArgumentTypeMismatchException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        String template = "Request path parameter '%s' must be a valid '%s'";
        String message = template.formatted(ex.getName(), ex.getRequiredType().getSimpleName());

        return ErrorResponseFactory.build(
            request,
            HttpStatus.BAD_REQUEST,
            message);
    }

    /**
     * Handle thrown {@link HttpMessageNotReadableException} by converting into an
     * {@link ErrorResponse} with status {@link HttpStatus.BAD_REQUEST}.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpServletRequest request,
        HttpMessageNotReadableException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        String raw = ex.getMessage();
        String message = (raw.contains("Required request body is missing"))
            ? "Required request body is missing"
            : "Required request body is malformed";

        return ErrorResponseFactory.build(
            request,
            HttpStatus.BAD_REQUEST,
            message);
    }

    /**
     * Handle thrown {@link NoResourceFoundException} by converting into an
     * {@link ErrorResponse} with status {@link HttpStatus.NOT_FOUND}.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ErrorResponse> handleNoResourceFoundException(
        HttpServletRequest request,
        NoResourceFoundException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        String message = "Resource not found";

        return ErrorResponseFactory.build(
            request,
            HttpStatus.NOT_FOUND,
            message);
    }

    /**
     * Handle thrown {@link HttpRequestMethodNotSupportedException} by converting
     * into an {@link ErrorResponse} with status
     * {@link HttpStatus.METHOD_NOT_ALLOWED}, and appends the allowed methods to the
     * {@link HttpHeaders.ALLOW} header.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
        HttpServletRequest request,
        HttpServletResponse response,
        HttpRequestMethodNotSupportedException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        var supported = ex.getSupportedHttpMethods();
        if (supported != null && !supported.isEmpty()) {
            String allowed = supported.stream().map(HttpMethod::name).collect(Collectors.joining(", "));
            response.setHeader(HttpHeaders.ALLOW, allowed);
        }

        String template = "Unsupported HTTP Method: %s";
        String message = template.formatted(request.getMethod());

        return ErrorResponseFactory.build(
            request,
            HttpStatus.METHOD_NOT_ALLOWED,
            message);
    }

    /**
     * Handle thrown {@link HttpMediaTypeNotSupportedException} by converting into
     * an {@link ErrorResponse} with status
     * {@link HttpStatus.UNSUPPORTED_MEDIA_TYPE}.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
        HttpServletRequest request,
        HttpMediaTypeNotSupportedException ex) {
        log.debug("Handling exception: {}", ex.getMessage());

        String template = "Unsupported Content-Type: %s";
        String message = template.formatted(ex.getContentType());

        return ErrorResponseFactory.build(
            request,
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            message);
    }

}
