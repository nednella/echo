package com.example.echo_api.modules.clerk.exception;

import org.springframework.http.HttpStatus;

import com.example.echo_api.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClerkErrorCode implements ErrorCode {

    WEBHOOK_PAYLOAD_INVALID(HttpStatus.BAD_REQUEST, "%s", 1),
    WEBHOOK_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "Invalid webhook signature", 0),
    SDK_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "There was an issue when communicating with the Clerk SDK: %s", 1);

    private final HttpStatus status;
    private final String messageTemplate;
    private final int expectedArgs;

}
