package com.example.echo_api.shared.constant;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ApiRoutes {

    private static final String BASE_URL = "/v1";

    @NoArgsConstructor(access = PRIVATE)
    public static final class CLERK {

        private static final String ROOT = BASE_URL + "/clerk";
        public static final String ONBOARDING = ROOT + "/onboarding";
        public static final String WEBHOOK = ROOT + "/webhook";
        public static final String PERSIST_ALL = ROOT + "/persist-all"; // dev only
        public static final String SYNC_ALL = ROOT + "/sync-all"; // dev only

    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class PROFILE {

        private static final String ROOT = BASE_URL + "/profile";
        public static final String ME = ROOT + "/me";
        public static final String BY_USERNAME = ROOT + "/{username}";
        public static final String FOLLOWERS = ROOT + "/{id}/followers";
        public static final String FOLLOWING = ROOT + "/{id}/following";
        public static final String FOLLOW = ROOT + "/{id}/follow";

    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class POST {

        private static final String ROOT = BASE_URL + "/post";
        public static final String CREATE = ROOT;
        public static final String BY_ID = ROOT + "/{id}";
        public static final String LIKE = ROOT + "/{id}/like";
        public static final String REPLIES = ROOT + "/{id}/replies";

    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class FEED {

        private static final String ROOT = BASE_URL + "/feed";
        public static final String HOMEPAGE = ROOT + "/homepage";
        public static final String DISCOVER = ROOT + "/discover";
        public static final String POSTS = ROOT + "/profile/{id}/posts";
        public static final String REPLIES = ROOT + "/profile/{id}/replies";
        public static final String LIKES = ROOT + "/profile/{id}/likes";
        public static final String MENTIONS = ROOT + "/profile/{id}/mentions";

    }

}
