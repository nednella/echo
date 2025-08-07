package com.example.echo_api.config;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * Config class for defining error messages across the application.
 */
@NoArgsConstructor(access = PRIVATE)
public final class ErrorMessageConfig {

    /* 400 */
    @NoArgsConstructor(access = PRIVATE)
    public static class BadRequest {

        // Validation
        public static final String INVALID_REQUEST = "Invalid request";

        // Authentication
        public static final String ONBOARDING_COMPLETED = "User has already completed the onboarding process";
        public static final String CLERK_ID_ALREADY_EXISTS = "Clerk user ID already exists in the local system";

    }

    /* 401 */
    @NoArgsConstructor(access = PRIVATE)
    public static class Unauthorised {

        public static final String MISSING_AUTHENTICATION = "Authentication required";
        public static final String INVALID_BEARER_TOKEN = "Invalid bearer token";
        public static final String INVALID_WEBHOOK_SIGNATURE = "Invalid webhook signature";

    }

    /* 403 */
    @NoArgsConstructor(access = PRIVATE)
    public static class Forbidden {

        public static final String INVALID_AUTH_PRINCIPAL = "Missing or invalid authentication principal, contact support for assistance";
        public static final String METADATA_MISSING = "Token metadata is missing, contact support for assistance";
        public static final String ONBOARDING_NOT_COMPLETED = "User has not completed the onboarding process";
        public static final String ECHO_ID_MISSING_OR_MALFORMED = "Echo ID is missing or malformed, contact support for assistance";

        public static final String ACCESS_DENIED = "You are missing permissions required to perform this action";
        public static final String RESOURCE_OWNERSHIP_REQUIRED = "Resource ownership is required for performing this action";

    }

    /* 404 */
    @NoArgsConstructor(access = PRIVATE)
    public static class NotFound {

        public static final String RESOURCE_NOT_FOUND = "Resource not found";

    }

    /* 409 */
    @NoArgsConstructor(access = PRIVATE)
    public static class Conflict {

        public static final String ALREADY_FOLLOWING = "You are already following this profile";
        public static final String ALREADY_LIKED = "You have already liked this post";
        public static final String SELF_ACTION = "You cannot perform this action on yourself";

    }

    /* 500 */
    @NoArgsConstructor(access = PRIVATE)
    public static class InternalServerError {

        public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred";
        public static final String CLERK_SDK_ERROR = "An error occurred whilst interacting with the Clerk API";
        public static final String CLOUDINARY_SDK_ERROR = "An error occurred whilst interacting with the Cloudinary API";
        public static final String TWITTER_TEXT_ENUM_CONVERSION = "Failed to convert TwitterText entity type";

    }

}
