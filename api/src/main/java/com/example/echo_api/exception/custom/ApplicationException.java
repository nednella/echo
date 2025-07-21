package com.example.echo_api.exception.custom;

import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;
    private final String details;

    protected ApplicationException(HttpStatus httpStatus, String message, String details) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.details = details;
    }

    protected ApplicationException(HttpStatus httpStatus, String message) {
        this(httpStatus, message, null);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

}
