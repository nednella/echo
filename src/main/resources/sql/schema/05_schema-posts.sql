CREATE TABLE
    IF NOT EXISTS "post" (
        id                  UUID PRIMARY KEY,
        conversation_id     UUID NOT NULL,
        author_id           UUID NOT NULL,
        text                VARCHAR(140) NOT NULL,
        created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        parent_id           UUID, -- existence indicates post is a reply
        repost_id           UUID, -- existence indicates post is a repost
        is_quoted_repost    BOOLEAN, -- existence describes type of repost
        CONSTRAINT fk_parent_id FOREIGN KEY (parent_id) REFERENCES "post"(id),
        CONSTRAINT fk_conversation_id FOREIGN KEY (conversation_id) REFERENCES "post"(id),
        CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES "profile"(id)
    );

CREATE INDEX
    IF NOT EXISTS idx_post_parent_id
        ON "post"(parent_id);

CREATE INDEX
    IF NOT EXISTS idx_post_conversation_id
        ON "post"(conversation_id);

CREATE INDEX
    IF NOT EXISTS idx_post_author_id
        ON "post"(author_id);

CREATE INDEX
    IF NOT EXISTS idx_post_created_at
        ON "post"(created_at);

CREATE INDEX
    IF NOT EXISTS idx_post_parent_id_created_at
        ON "post"(parent_id, created_at);

CREATE INDEX
    IF NOT EXISTS idx_post_author_id_created_at
        ON "post"(author_id, created_at);