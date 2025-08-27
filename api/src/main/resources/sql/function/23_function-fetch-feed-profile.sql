
/*
 * Function fetches posts that form a users profile page.
 *
 * Root-level posts belonging to the user are fetched and sorted by creation timestamp.
 * 
*/

CREATE OR REPLACE FUNCTION fetch_feed_profile(
    p_profile_id UUID,
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
        WITH profile_posts AS (
            SELECT
                p.id
            FROM post p
            WHERE p.author_id = p_profile_id
            AND p.parent_id IS NULL
            ORDER BY p.created_at DESC
            OFFSET p_offset
            LIMIT p_limit
        )
        SELECT
            fp.*
        FROM fetch_posts(ARRAY(SELECT pp.id FROM profile_posts pp), p_authenticated_user_id) fp
        ORDER BY fp.created_at DESC;
    END;
'
LANGUAGE plpgsql;