CREATE TABLE
    IF NOT EXISTS "profile" (
        id                  UUID PRIMARY KEY,
        username            VARCHAR(255) UNIQUE NOT NULL,
        image_url           VARCHAR(255),
        name                VARCHAR(50),
        bio                 VARCHAR(160),
        location            VARCHAR(30),
        created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES "user"(id) ON DELETE CASCADE,
        CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES "user"(username) ON UPDATE CASCADE
    );

CREATE UNIQUE INDEX 
    IF NOT EXISTS idx_profile_username
        ON "profile"(Lower(username));
