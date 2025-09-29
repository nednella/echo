package com.example.echo_api.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public ApplicationException(ErrorCode error, Object... args) {
        super(error.formatMessage(args)); // set RuntimeException message
        this.status = error.getStatus();
        this.message = error.formatMessage(args);
    }

}
