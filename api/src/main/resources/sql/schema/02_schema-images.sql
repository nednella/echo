CREATE TABLE
    IF NOT EXISTS "image" (
        id                 UUID PRIMARY KEY,
        image_type         VARCHAR(20) NOT NULL,
        public_id          VARCHAR(255) NOT NULL,
        asset_id           VARCHAR(255) NOT NULL,
        original_width     INTEGER NOT NULL,
        original_height    INTEGER NOT NULL,
        original_url       VARCHAR(255) NOT NULL,
        transformed_url    VARCHAR(255) NOT NULL,
        created_at         TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- TODO: delete and replace w/ frontend uploads via signed urls. only store urls in db