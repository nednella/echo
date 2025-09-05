package com.example.echo_api.modules.profile.exception;

import org.springframework.http.HttpStatus;

import com.example.echo_api.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProfileErrorCode implements ErrorCode {

    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "Profile with id '%s' not found", 1),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "Profile with username '%s' not found", 1),
    ALREADY_FOLLOWING(HttpStatus.CONFLICT, "Already following profile with id '%s'", 1),
    SELF_ACTION(HttpStatus.CONFLICT, "You cannot perform this action on your own profile", 0);

    private final HttpStatus status;
    private final String messageTemplate;
    private final int expectedArgs;

}
