package com.example.echo_api.modules.user.exception;

import org.springframework.http.HttpStatus;

import com.example.echo_api.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    ;

    private final HttpStatus status;
    private final String messageTemplate;
    private final int expectedArgs;

}