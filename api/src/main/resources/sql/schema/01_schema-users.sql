CREATE TABLE
    IF NOT EXISTS "user" (
        id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        clerk_id      VARCHAR(255) UNIQUE NOT NULL,
        username      VARCHAR(255) UNIQUE NOT NULL, -- validated w/ Clerk
        enabled       BOOLEAN DEFAULT TRUE,
        created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE UNIQUE INDEX
    IF NOT EXISTS idx_user_clerk_id
        ON "user"(LOWER(clerk_id));

CREATE UNIQUE INDEX
    IF NOT EXISTS idx_user_email
        ON "user"(LOWER(email));

CREATE UNIQUE INDEX
    IF NOT EXISTS idx_user_username
        ON "user"(LOWER(username));
