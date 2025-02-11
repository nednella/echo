package com.example.echo_api.config;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * 
 */
@NoArgsConstructor(access = PRIVATE)
public final class ErrorMessageConfig {

    /* 400 BAD REQUEST */
    /* Validation */
    public static final String INVALID_REQUEST = "Invalid request.";
    /* Auth */
    public static final String USERNAME_OR_PASSWORD_IS_INCORRECT = "Username or password is incorrect.";
    /* Account */
    public static final String ID_NOT_FOUND = "ID not found.";
    /* Username */
    public static final String USERNAME_NOT_FOUND = "Username not found.";
    public static final String USERNAME_ARLEADY_EXISTS = "Username already exists.";
    /* Password */
    public static final String INCORRECT_CURRENT_PASSWORD = "Incorrect current password.";
    /* Relationship */
    public static final String ALREADY_FOLLOWING = "You are already following this profile.";
    public static final String NOT_FOLLOWING = "You are not following this profile.";
    public static final String ALREADY_BLOCKING = "You are already blocking this profile.";
    public static final String NOT_BLOCKING = "You are not blocking this profile.";
    public static final String SELF_FOLLOW = "You cannot follow yourself.";
    public static final String SELF_UNFOLLOW = "You cannot unfollow yourself.";
    public static final String SELF_BLOCK = "You cannot block yourself.";
    public static final String SELF_UNBLOCK = "You cannot unblock yourself.";

    /* 401 UNAUTHORISED */
    public static final String UNAUTHORISED = "Unauthorised request.";
    public static final String ACCOUNT_STATUS = "Account status is abnormal.";
    public static final String BLOCKED = "Action prohibited - this user has blocked you.";

    /* 403 FORBIDDEN */
    public static final String FORBIDDEN = "Access denied.";

    /* 404 RESOURCE NOT FOUND */
    public static final String NOT_FOUND = "Resource not found.";

    /* 500 INTERNAL SERVER ERROR */
    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred.";

}
