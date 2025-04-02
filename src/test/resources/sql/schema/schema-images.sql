CREATE TABLE
    IF NOT EXISTS "image" (
        image_id           UUID PRIMARY KEY,
        image_type         VARCHAR(20) NOT NULL,
        public_id          VARCHAR(255) NOT NULL,
        asset_id           VARCHAR(255) NOT NULL,
        original_width     INTEGER NOT NULL,
        original_height    INTEGER NOT NULL,
        original_url       VARCHAR(255) NOT NULL,
        transformed_url    VARCHAR(255) NOT NULL,
        created_at         TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
    );
