CREATE TABLE
    IF NOT EXISTS "post_like" (
        post_id       UUID NOT NULL,
        author_id     UUID NOT NULL,
        created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (post_id, author_id),
        CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES "post"(id) ON DELETE CASCADE,
        CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES "profile"(id) ON DELETE CASCADE
    );

CREATE INDEX
    IF NOT EXISTS idx_post_like_post_id -- count likes per post
        ON "post_like"(post_id);

CREATE INDEX
    IF NOT EXISTS idx_post_like_author_id -- count posts liked by author
        ON "post_like"(author_id);

CREATE INDEX
    IF NOT EXISTS idx_post_like_author_created_at -- retrieve liked posts by author sorted by creation
        ON "post_like"(author_id, created_at);