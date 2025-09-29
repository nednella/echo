CREATE TABLE
    IF NOT EXISTS "user" (
        id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        external_id    VARCHAR(255) UNIQUE NOT NULL,
        status         VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
        created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE UNIQUE INDEX
    IF NOT EXISTS idx_user_external_id
        ON "user"(LOWER(external_id));

CREATE INDEX
    IF NOT EXISTS idx_user_status
        ON "user"(status);
