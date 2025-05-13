CREATE TABLE
    IF NOT EXISTS "follow" (
        follower_id    UUID NOT NULL,
        followed_id    UUID NOT NULL,
        created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (follower_id, followed_id),
        CONSTRAINT fk_follower_id FOREIGN KEY (follower_id) REFERENCES "profile"(id) ON DELETE CASCADE,
        CONSTRAINT fk_followed_id FOREIGN KEY (followed_id) REFERENCES "profile"(id) ON DELETE CASCADE,
        CONSTRAINT no_self_follow CHECK (follower_id != followed_id)
    );

CREATE INDEX 
    IF NOT EXISTS idx_follow_follower_id
        ON "follow"(follower_id);

CREATE INDEX 
    IF NOT EXISTS idx_follow_followed_id
        ON "follow"(followed_id);

CREATE TABLE
    IF NOT EXISTS "block" (
        blocker_id    UUID NOT NULL,
        blocked_id    UUID NOT NULL,
        created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (blocker_id, blocked_id),
        CONSTRAINT fk_blocker_id FOREIGN KEY (blocker_id) REFERENCES "profile"(id) ON DELETE CASCADE,
        CONSTRAINT fk_blocked_id FOREIGN KEY (blocked_id) REFERENCES "profile"(id) ON DELETE CASCADE,
        CONSTRAINT no_self_block CHECK (blocker_id != blocked_id)
    );

CREATE INDEX 
    IF NOT EXISTS idx_block_blocker_id
        ON "block"(blocker_id);

CREATE INDEX 
    IF NOT EXISTS idx_block_blocked_id
        ON "block"(blocked_id);

