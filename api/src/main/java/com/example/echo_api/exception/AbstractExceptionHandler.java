package com.example.echo_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import com.example.echo_api.shared.dto.ErrorDTO;

import jakarta.servlet.http.HttpServletRequest;

/**
 * TODO: jdoc
 */
public abstract class AbstractExceptionHandler {

    /**
     * Creates and returns an {@link ErrorDTO}, providing uniform JSON error
     * responses across the application.
     * 
     * <p>
     * The returned error will follow the structure:
     * 
     * <pre>
     * {
     *      "timestamp": ,
     *      "status": ,
     *      "message": ,
     *      "path": 
     * }
     * </pre>
     * 
     * @param request the incoming HTTP request that resulted in the exception
     * @param status  the HTTP status code associated with the error
     * @param message a short description for the cause of the error
     * @return a {@link ResponseEntity} containing an {@link ErrorDTO} with the
     *         generated error details
     * 
     * @see ErrorDTO
     */
    protected ResponseEntity<ErrorDTO> handleException(
        @NonNull HttpServletRequest request,
        @NonNull HttpStatus status,
        @NonNull String message) {

        ErrorDTO error = new ErrorDTO(
            status,
            message,
            request.getRequestURI());

        return ResponseEntity
            .status(status)
            .body(error);
    }

}