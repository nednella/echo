package com.example.echo_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.example.echo_api.persistence.dto.response.error.ErrorDTO;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Abstract class that provides a method for generating standardised error
 * responses in the event of a thrown exception in the application.
 * 
 * <p>
 * The class should be extended by ControllerAdvice classes that handle
 * controller-specific exceptions and used to map those exceptions to an
 * appropriate HTTP response.
 * 
 */
public abstract class AbstractControllerAdvice {

    /**
     * Creates and returns a custom {@link ErrorDTO}, providing uniform JSON
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
     *      "details": ,  // optional field that can be omitted with null
     *      "path": 
     * }
     * </pre>
     * 
     * @param request The incoming HTTP request that resulted in the exception.
     * @param status  The HTTP status code to be returned in the response.
     * @param message A message describing the error.
     * @param details An optional object containing additional information on the
     *                error.
     * @return A {@link ResponseEntity} containing an {@link ErrorWrapper} with the
     *         generated error details.
     * 
     * @see ErrorDTO
     */
    protected ResponseEntity<ErrorDTO> createExceptionHandler(
        @NonNull HttpServletRequest request,
        @NonNull HttpStatus status,
        @NonNull String message,
        @Nullable String details) {

        ErrorDTO error = new ErrorDTO(
            status,
            message,
            details,
            request.getRequestURI());

        return ResponseEntity
            .status(status)
            .body(error);
    }

}