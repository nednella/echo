/*
 * db migration initialisation
 */

-- 1) tables

CREATE TABLE users (
    id             UUID PRIMARY KEY,
    external_id    VARCHAR(255) NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE profiles (
    id            UUID PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    name          VARCHAR(50),
    bio           VARCHAR(160),
    location      VARCHAR(30),
    image_url     VARCHAR(255),
    created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE profile_follows (
    follower_id    UUID NOT NULL,
    followed_id    UUID NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, followed_id),
    CONSTRAINT fk_follower_id FOREIGN KEY (follower_id) REFERENCES profiles(id) ON DELETE CASCADE,
    CONSTRAINT fk_followed_id FOREIGN KEY (followed_id) REFERENCES profiles(id) ON DELETE CASCADE,
    CONSTRAINT no_self_follow CHECK (follower_id != followed_id)
);

CREATE TABLE posts (
    id                 UUID PRIMARY KEY,
    parent_id          UUID,
    conversation_id    UUID NOT NULL,
    author_id          UUID NOT NULL,
    text               VARCHAR(280) NOT NULL,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_parent_id FOREIGN KEY (parent_id) REFERENCES posts(id),
    CONSTRAINT fk_conversation_id FOREIGN KEY (conversation_id) REFERENCES posts(id),
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES profiles(id)
);

CREATE TABLE post_likes (
    post_id       UUID NOT NULL,
    author_id     UUID NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id, author_id),
    CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES profiles(id) ON DELETE CASCADE
);

CREATE TABLE post_entities (
    post_id        UUID NOT NULL,
    entity_type    VARCHAR(20) NOT NULL,
    start_index    INTEGER NOT NULL,
    end_index      INTEGER NOT NULL,
    text           VARCHAR(280) NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id, entity_type, start_index),
    CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

-- 2) indexes

CREATE UNIQUE INDEX idx_users_external_id ON users(LOWER(external_id)); -- enforce uniqueness on case-insensitive ext. id
CREATE INDEX idx_users_status ON users(status);

CREATE UNIQUE INDEX idx_profiles_username ON profiles(Lower(username)); -- enforce uniqueness on case-insensitive username

CREATE INDEX idx_profiles_follow_follower_id ON profile_follows(follower_id);
CREATE INDEX idx_profiles_follow_followed_id ON profile_follows(followed_id);

CREATE INDEX idx_posts_parent_id ON posts(parent_id);
CREATE INDEX idx_posts_conversation_id ON posts(conversation_id);
CREATE INDEX idx_posts_author_id ON posts(author_id);
CREATE INDEX idx_posts_created_at ON posts(created_at);
CREATE INDEX idx_posts_parent_id_created_at ON posts(parent_id, created_at);
CREATE INDEX idx_posts_author_id_created_at ON posts(author_id, created_at);

CREATE INDEX idx_post_likes_post_id ON post_likes(post_id); -- count likes per post 
CREATE INDEX idx_post_likes_author_id ON post_likes(author_id); -- count posts liked by author 
CREATE INDEX idx_post_likes_author_created_at ON post_likes(author_id, created_at); -- retrieve liked posts by author sorted by creation 

CREATE INDEX idx_post_entities_post_id ON post_entities(post_id); -- retrieve entities by post
CREATE INDEX idx_post_entities_text ON post_entities(LOWER(text)); -- retrieve posts where entity = ...
CREATE INDEX idx_post_entities_type_text ON post_entities(entity_type, LOWER(text)); -- retrieve posts where entity type = ... && entity = ...