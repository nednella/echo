CREATE TABLE
    IF NOT EXISTS "post_entity" (
        post_id        UUID NOT NULL,
        entity_type    VARCHAR(20) NOT NULL,
        start_index    INTEGER NOT NULL,
        end_index      INTEGER NOT NULL,
        text           VARCHAR(140) NOT NULL,
        created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (post_id, entity_type, start_index),
        CONSTRAINT fk_post_id FOREIGN KEY (post_id) REFERENCES "post"(id) ON DELETE CASCADE
    );

CREATE INDEX
    IF NOT EXISTS idx_post_entity_post_id -- retrieve entities by post
        ON "post_entity"(post_id);

CREATE INDEX
    IF NOT EXISTS idx_post_entity_text -- retrieve posts where entity = ...
        ON "post_entity"(LOWER(text));

CREATE INDEX
    IF NOT EXISTS idx_post_entity_type_text -- retrieve posts where entity type = ... && entity = ...
        ON "post_entity"(entity_type, LOWER(text));