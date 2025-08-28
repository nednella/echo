package com.example.echo_api.config;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * Config class for defining API endpoint path constants used throughout the
 * controller package.
 */
@NoArgsConstructor(access = PRIVATE)
public final class ApiConfig {

    private static final String BASE_URL = "/api/v1";

    @NoArgsConstructor(access = PRIVATE)
    public static final class Clerk {

        private static final String ROOT = BASE_URL + "/clerk";
        public static final String ONBOARDING = ROOT + "/onboarding";
        public static final String WEBHOOK = ROOT + "/webhook"; // PUBLIC ENDPOINT

    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class Dev {

        private static final String ROOT = BASE_URL + "/dev";
        public static final String PERSIST_ALL = ROOT + "/clerk/persist-all";
        public static final String SYNC_ALL = ROOT + "/clerk/sync-all";

    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class Profile {

        private static final String ROOT = BASE_URL + "/profile";
        public static final String ME = ROOT + "/me";
        public static final String GET_BY_USERNAME = ROOT + "/{username}";
        public static final String GET_FOLLOWERS_BY_ID = ROOT + "/{id}/followers";
        public static final String GET_FOLLOWING_BY_ID = ROOT + "/{id}/following";
        public static final String FOLLOW_BY_ID = ROOT + "/{id}/follow";

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
        public static final String POSTS_BY_PROFILE_ID = ROOT + "/profile/{id}/posts";
        public static final String REPLIES_BY_PROFILE_ID = ROOT + "/profile/{id}/replies";
        public static final String LIKES_BY_PROFILE_ID = ROOT + "/profile/{id}/likes";
        public static final String MENTIONS_OF_PROFILE_ID = ROOT + "/profile/{id}/mentions";

    }

}
