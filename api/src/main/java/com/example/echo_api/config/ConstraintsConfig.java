package com.example.echo_api.config;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * Config class for defining integer-based constraints across the application.
 */
@NoArgsConstructor(access = PRIVATE)
public class ConstraintsConfig {

    @NoArgsConstructor(access = PRIVATE)
    public static class Account {
        public static final int USERNAME_MIN_LENGTH = 3;
        public static final int USERNAME_MAX_LENGTH = 15;
        public static final int PASSWORD_MIN_LENGTH = 6;
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Profile {
        public static final int NAME_MAX_LENGTH = 50;
        public static final int BIO_MAX_LENGTH = 160;
        public static final int LOCATION_MAX_LENGTH = 30;
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Post {
        public static final int TEXT_MAX_LENGTH = 280;
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Pagination {
        public static final int MIN_OFFSET = 0;
        public static final int MIN_LIMIT = 1;
        public static final int MAX_LIMIT = 50;
    }

}
