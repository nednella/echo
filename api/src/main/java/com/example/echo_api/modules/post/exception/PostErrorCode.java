package com.example.echo_api.modules.post.exception;

import org.springframework.http.HttpStatus;

import com.example.echo_api.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    INVALID_PARENT_ID(HttpStatus.BAD_REQUEST, "Parent post with id '%s' does not exist", 1),
    POST_NOT_OWNED(HttpStatus.FORBIDDEN, "Only the post author can perform this action", 0),
    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "Post with id '%s' not found", 1),
    ALREADY_LIKED(HttpStatus.CONFLICT, "Already liked post with id '%s'", 1);

    private final HttpStatus status;
    private final String messageTemplate;
    private final int expectedArgs;

}
