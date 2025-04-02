CREATE TABLE
    IF NOT EXISTS "profile" (
        id            UUID PRIMARY KEY,
        username      VARCHAR(15) UNIQUE NOT NULL,
        name          VARCHAR(50),
        bio           VARCHAR(160),
        location      VARCHAR(30),
        avatar_id     UUID,
        banner_id     UUID,
        created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES "account"(id) ON DELETE CASCADE,
        CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES "account"(username) ON UPDATE CASCADE,
        CONSTRAINT fk_avatar_id FOREIGN KEY (avatar_id) REFERENCES "image"(image_id) ON DELETE SET NULL,
        CONSTRAINT fk_banner_id FOREIGN KEY (banner_id) REFERENCES "image"(image_id) ON DELETE SET NULL
    );

CREATE UNIQUE INDEX 
    IF NOT EXISTS idx_profile_username
        ON "profile"(Lower(username));
