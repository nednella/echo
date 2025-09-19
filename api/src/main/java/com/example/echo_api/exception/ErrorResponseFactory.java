package com.example.echo_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import jakarta.servlet.http.HttpServletRequest;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ErrorResponseFactory {

    /**
     * Build and return an {@link ErrorResponse}, providing uniform JSON error
     * response across the API.
     * 
     * @param request the incoming HTTP request that resulted in the error
     * @param status  the HTTP status code associated with the error
     * @param message a short description for the cause of the error
     * @return a {@link ResponseEntity} containing the {@link ErrorResponse}
     */
    public static ResponseEntity<ErrorResponse> build(
        @NonNull HttpServletRequest request,
        @NonNull HttpStatus status,
        @NonNull String message) {

        ErrorResponse err = new ErrorResponse(
            status,
            message,
            request.getRequestURI());

        return ResponseEntity
            .status(status)
            .body(err);
    }

}
