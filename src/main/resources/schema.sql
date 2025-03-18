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

CREATE TABLE
    IF NOT EXISTS "profile" (
        profile_id    UUID PRIMARY KEY,
        username      VARCHAR(15) UNIQUE NOT NULL,
        name          VARCHAR(50),
        bio           VARCHAR(160),
        location      VARCHAR(30),
        avatar_id     UUID,
        banner_id     UUID,
        created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_profile_id FOREIGN KEY (profile_id) REFERENCES "account"(id) ON DELETE CASCADE,
        CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES "account"(username) ON UPDATE CASCADE,
        CONSTRAINT fk_avatar_id FOREIGN KEY (avatar_id) REFERENCES "image"(image_id) ON DELETE SET NULL,
        CONSTRAINT fk_banner_id FOREIGN KEY (banner_id) REFERENCES "image"(image_id) ON DELETE SET NULL
    );

CREATE UNIQUE INDEX 
    IF NOT EXISTS idx_profile_username
        ON "profile"(Lower(username));

CREATE TABLE
    IF NOT EXISTS "follow" (
        follower_id     UUID NOT NULL,
        following_id    UUID NOT NULL,
        created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (follower_id, following_id),
        CONSTRAINT fk_follower_id FOREIGN KEY (follower_id) REFERENCES "profile"(profile_id) ON DELETE CASCADE,
        CONSTRAINT fk_following_id FOREIGN KEY (following_id) REFERENCES "profile"(profile_id) ON DELETE CASCADE,
        CONSTRAINT no_self_follow CHECK (follower_id != following_id)
    );

CREATE INDEX 
    IF NOT EXISTS idx_follow_follower_id
        ON "follow"(follower_id);

CREATE INDEX 
    IF NOT EXISTS idx_follow_following_id
        ON "follow"(following_id);

CREATE TABLE
    IF NOT EXISTS "block" (
        blocker_id     UUID NOT NULL,
        blocking_id    UUID NOT NULL,
        created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (blocker_id, blocking_id),
        CONSTRAINT fk_blocker_id FOREIGN KEY (blocker_id) REFERENCES "profile"(profile_id) ON DELETE CASCADE,
        CONSTRAINT fk_blocking_id FOREIGN KEY (blocking_id) REFERENCES "profile"(profile_id) ON DELETE CASCADE,
        CONSTRAINT no_self_block CHECK (blocker_id != blocking_id)
    );

CREATE INDEX 
    IF NOT EXISTS idx_block_blocker_id
        ON "block"(blocker_id);

CREATE INDEX 
    IF NOT EXISTS idx_block_blocking_id
        ON "block"(blocking_id);
