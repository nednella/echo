/*
 * db migration initialisation
 */

-- 1) tables

CREATE TABLE "user" (
    id             UUID PRIMARY KEY,
    external_id    VARCHAR(255) NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "profile" (
    id            UUID PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    name          VARCHAR(50),
    bio           VARCHAR(160),
    location      VARCHAR(30),
    image_url     VARCHAR(255),
    created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE "profile_follow" (
    follower_id    UUID NOT NULL,
    followed_id    UUID NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, followed_id),
    CONSTRAINT fk_follower_id FOREIGN KEY (follower_id) REFERENCES "profile"(id) ON DELETE CASCADE,
    CONSTRAINT fk_followed_id FOREIGN KEY (followed_id) REFERENCES "profile"(id) ON DELETE CASCADE,
    CONSTRAINT no_self_follow CHECK (follower_id != followed_id)
);

CREATE TABLE "post" (
    id                 UUID PRIMARY KEY,
    parent_id          UUID,
    conversation_id    UUID NOT NULL,
    author_id          UUID NOT NULL,
    text               VARCHAR(280) NOT NULL,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_parent_id FOREIGN KEY (parent_id) REFERENCES "post"(id),
    CONSTRAINT fk_conversation_id FOREIGN KEY (conversation_id) REFERENCES "post"(id),
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES "profile"(id)
);

CREATE TABLE "post_like" (
    post_id       UUID NOT NULL,
    author_id     UUID NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id, author_id),
    CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES "post"(id) ON DELETE CASCADE,
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES "profile"(id) ON DELETE CASCADE
);

CREATE TABLE "post_entity" (
    post_id        UUID NOT NULL,
    entity_type    VARCHAR(20) NOT NULL,
    start_index    INTEGER NOT NULL,
    end_index      INTEGER NOT NULL,
    text           VARCHAR(280) NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id, entity_type, start_index),
    CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES "post"(id) ON DELETE CASCADE
);

-- 2) indexes

CREATE UNIQUE INDEX idx_user_external_id ON "user" (LOWER(external_id)); -- enforce uniqueness on case-insensitive ext. id
CREATE INDEX idx_user_status ON "user" (status);

CREATE UNIQUE INDEX idx_profile_username ON "profile" (Lower(username)); -- enforce uniqueness on case-insensitive username

CREATE INDEX idx_profile_follow_follower_id ON "profile_follow" (follower_id);
CREATE INDEX idx_profile_follow_followed_id ON "profile_follow" (followed_id);

CREATE INDEX idx_post_parent_id ON "post"(parent_id);
CREATE INDEX idx_post_conversation_id ON "post"(conversation_id);
CREATE INDEX idx_post_author_id ON "post"(author_id);
CREATE INDEX idx_post_created_at ON "post"(created_at);
CREATE INDEX idx_post_parent_id_created_at ON "post"(parent_id, created_at);
CREATE INDEX idx_post_author_id_created_at ON "post"(author_id, created_at);

CREATE INDEX idx_post_like_post_id ON "post_like"(post_id); -- count likes per post 
CREATE INDEX idx_post_like_author_id ON "post_like"(author_id); -- count posts liked by author 
CREATE INDEX idx_post_like_author_created_at ON "post_like"(author_id, created_at); -- retrieve liked posts by author sorted by creation 

CREATE INDEX idx_post_entity_post_id ON "post_entity"(post_id); -- retrieve entities by post
CREATE INDEX idx_post_entity_text ON "post_entity"(LOWER(text)); -- retrieve posts where entity = ...
CREATE INDEX idx_post_entity_type_text ON "post_entity"(entity_type, LOWER(text)); -- retrieve posts where entity type = ... && entity = ...