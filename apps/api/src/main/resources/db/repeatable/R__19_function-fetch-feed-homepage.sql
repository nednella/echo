
/*
 * Function fetches posts that form a users homepage.
 *
 * Root-level posts belonging to the user, and any users followed by the user, are fetched
 * and sorted by creation timestamp.
 * 
 */

CREATE OR REPLACE FUNCTION fetch_feed_homepage(
    p_authenticated_user_id UUID,
    p_offset INTEGER,
    p_limit INTEGER
)
RETURNS TABLE (
    id                        UUID,
    parent_id                 UUID,
    conversation_id           UUID,
    text                      VARCHAR(280),
    created_at                TIMESTAMPTZ,
    post_like_count           BIGINT,
    post_reply_count          BIGINT,
    post_share_count          BIGINT,
    post_rel_liked            BOOLEAN,
    post_rel_shared           BOOLEAN,
    author_is_self            BOOLEAN,
    author_id                 UUID,
    author_username           VARCHAR(255),
    author_name               VARCHAR(50),
    author_image_url          VARCHAR(255),
    author_rel_following      BOOLEAN,
    author_rel_followed_by    BOOLEAN,
    post_entities             JSONB
)
AS
'
    BEGIN
        RETURN QUERY
        WITH homepage_posts AS (
            SELECT
                p.id
            FROM post p
            LEFT JOIN profile_follow f ON p.author_id = f.followed_id
            WHERE p.parent_id IS NULL
            AND (f.follower_id = p_authenticated_user_id
            OR p.author_id = p_authenticated_user_id)
            ORDER BY p.created_at DESC
            OFFSET p_offset
            LIMIT p_limit
        )
        SELECT
            fp.*
        FROM fetch_posts(ARRAY(SELECT hp.id FROM homepage_posts hp), p_authenticated_user_id) fp
        ORDER BY fp.created_at DESC;
    END;
'
LANGUAGE plpgsql;