CREATE TABLE
    IF NOT EXISTS "post_hashtag" (
        id             UUID PRIMARY KEY,
        post_id        UUID NOT NULL,
        start_index    INTEGER NOT NULL,
        end_index      INTEGER NOT NULL,
        text           TEXT NOT NULL,
        created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES "post"(id) ON DELETE CASCADE
    );

CREATE INDEX
    IF NOT EXISTS idx_post_hashtag_post_id_text -- retrieve hashtags by post
        ON "post_hashtag"(post_id);

CREATE INDEX
    IF NOT EXISTS idx_post_hashtag_post_id_text -- retrieve posts where hashtag = ...
        ON "post_hashtag"(post_id, LOWER(text));

CREATE TABLE
    IF NOT EXISTS "post_mention" (
        id             UUID PRIMARY KEY,
        post_id        UUID NOT NULL,
        start_index    INTEGER NOT NULL,
        end_index      INTEGER NOT NULL,
        text           TEXT NOT NULL,
        created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES "post"(id) ON DELETE CASCADE
    );

CREATE INDEX 
    IF NOT EXISTS idx_post_mention_post_id_text -- retrieve mentions by post
        ON "post_mention"(post_id);

CREATE INDEX 
    IF NOT EXISTS idx_post_mention_post_id_text -- retrieve posts where mention = ...
        ON "post_mention"(post_id, LOWER(text));