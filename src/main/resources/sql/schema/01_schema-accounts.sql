CREATE TABLE
    IF NOT EXISTS "account" (
        id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        username              VARCHAR(15) UNIQUE NOT NULL CHECK (username ~ '^[a-zA-Z0-9_]{3,15}$'),
        encrypted_password    VARCHAR(255) NOT NULL,
        role                  VARCHAR(255) NOT NULL DEFAULT USER,
        enabled               BOOLEAN NOT NULL DEFAULT TRUE,
        created_at            TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at            TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE UNIQUE INDEX 
    IF NOT EXISTS idx_account_username
        ON "account"(Lower(username));
