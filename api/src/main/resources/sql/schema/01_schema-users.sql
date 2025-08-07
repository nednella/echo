CREATE TABLE
    IF NOT EXISTS "user" (
        id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        clerk_id      VARCHAR(255) UNIQUE NOT NULL,
        username      VARCHAR(255) UNIQUE NOT NULL, -- validated w/ Clerk
        status        VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
        created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE UNIQUE INDEX
    IF NOT EXISTS idx_user_clerk_id
        ON "user"(LOWER(clerk_id));

CREATE INDEX
    IF NOT EXISTS idx_user_status
        ON "user"(status);
