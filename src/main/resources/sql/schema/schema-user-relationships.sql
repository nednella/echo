CREATE TABLE
    IF NOT EXISTS "follow" (
        follower_id     UUID NOT NULL,
        following_id    UUID NOT NULL,
        created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (follower_id, following_id),
        CONSTRAINT fk_follower_id FOREIGN KEY (follower_id) REFERENCES "profile"(id) ON DELETE CASCADE,
        CONSTRAINT fk_following_id FOREIGN KEY (following_id) REFERENCES "profile"(id) ON DELETE CASCADE,
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
        CONSTRAINT fk_blocker_id FOREIGN KEY (blocker_id) REFERENCES "profile"(id) ON DELETE CASCADE,
        CONSTRAINT fk_blocking_id FOREIGN KEY (blocking_id) REFERENCES "profile"(id) ON DELETE CASCADE,
        CONSTRAINT no_self_block CHECK (blocker_id != blocking_id)
    );

CREATE INDEX 
    IF NOT EXISTS idx_block_blocker_id
        ON "block"(blocker_id);

CREATE INDEX 
    IF NOT EXISTS idx_block_blocking_id
        ON "block"(blocking_id);
