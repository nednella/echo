CREATE TABLE
    IF NOT EXISTS "profile" (
        id                  UUID PRIMARY KEY,
        username            VARCHAR(255) UNIQUE NOT NULL,
        name                VARCHAR(50),
        bio                 VARCHAR(160),
        location            VARCHAR(30),
        image_url           VARCHAR(255),
        created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES "user"(id) ON DELETE CASCADE
    );

CREATE UNIQUE INDEX 
    IF NOT EXISTS idx_profile_username
        ON "profile"(Lower(username));
