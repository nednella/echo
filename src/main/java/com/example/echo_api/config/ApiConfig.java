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
        public static final String ME_INFO = ROOT + "/me/info"; // TODO: consolidate into single PUT to /me
        public static final String ME_AVATAR = ROOT + "/me/avatar"; // TODO: consolidate into single PUT to /me
        public static final String ME_BANNER = ROOT + "/me/banner"; // TODO: consolidate into single PUT to /me
        public static final String GET_BY_USERNAME = ROOT + "/{username}";
        public static final String GET_FOLLOWERS_BY_USERNAME = ROOT + "/{username}/followers"; // TODO: migrate to {id}
        public static final String GET_FOLLOWING_BY_USERNAME = ROOT + "/{username}/following"; // TODO: migrate to {id}
        public static final String FOLLOW_BY_USERNAME = ROOT + "/{username}/follow"; // TODO: migrate to {id}
        public static final String BLOCK_BY_USERNAME = ROOT + "/{username}/block"; // TODO: migrate to {id}
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class Post {
        private static final String ROOT = BASE_URL + "/post";
        public static final String CREATE = ROOT;
        public static final String LIKE = ROOT + "/{id}/like";
        public static final String GET_BY_ID = ROOT + "/{id}";
        public static final String GET_REPLIES_BY_ID = ROOT + "/{id}/replies";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class Feed {
        private static final String ROOT = BASE_URL + "/feed";
        public static final String HOMEPAGE = ROOT + "/homepage";
        public static final String DISCOVER = ROOT + "/discover";
        public static final String PROFILE_POSTS_BY_ID = ROOT + "/profile/{id}/posts";
        public static final String PROFILE_REPLIES_BY_ID = ROOT + "/profile/{id}/replies";
        public static final String PROFILE_LIKES_BY_ID = ROOT + "/profile/{id}/likes";
        public static final String PROFILE_MENTIONS_BY_ID = ROOT + "/profile/{id}/mentions";
    }

}
