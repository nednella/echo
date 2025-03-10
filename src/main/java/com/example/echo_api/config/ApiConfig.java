package com.example.echo_api.config;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * This class contains the path constants used throughout the application. It
 * defines the base URL for the API, along with all relevant endpoints.
 */
@NoArgsConstructor(access = PRIVATE)
public final class ApiConfig {

    private static final String BASE_URL = "/api/v1";

    @NoArgsConstructor(access = PRIVATE)
    public static final class Auth {
        private static final String ROOT = BASE_URL + "/auth";
        public static final String LOGIN = ROOT + "/login";
        public static final String SIGNUP = ROOT + "/signup";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class Account {
        private static final String ROOT = BASE_URL + "/account";
        public static final String USERNAME_AVAILABLE = ROOT + "/username-available";
        public static final String UPDATE_USERNAME = ROOT + "/username";
        public static final String UPDATE_PASSWORD = ROOT + "/password";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class Profile {
        private static final String ROOT = BASE_URL + "/profile";
        public static final String ME = ROOT + "/me";
        public static final String ME_INFO = ROOT + "/me/info";
        public static final String ME_AVATAR = ROOT + "/me/avatar";
        public static final String ME_BANNER = ROOT + "/me/banner";
        public static final String GET_BY_USERNAME = ROOT + "/{username}";
        public static final String GET_FOLLOWERS_BY_USERNAME = ROOT + "/{username}/followers";
        public static final String GET_FOLLOWING_BY_USERNAME = ROOT + "/{username}/following";
        public static final String FOLLOW_BY_USERNAME = ROOT + "/{username}/follow";
        public static final String UNFOLLOW_BY_USERNAME = ROOT + "/{username}/unfollow";
        public static final String BLOCK_BY_USERNAME = ROOT + "/{username}/block";
        public static final String UNBLOCK_BY_USERNAME = ROOT + "/{username}/unblock";
    }

}
