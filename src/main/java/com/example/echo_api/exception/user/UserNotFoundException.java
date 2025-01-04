package com.example.echo_api.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super(username + " not found.");
    }
}
