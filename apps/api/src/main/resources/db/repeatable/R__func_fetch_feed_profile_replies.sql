/* 
    R__func_fetch_feed_profile_replies.sql

    Profile Replies feed: selects posts authored by the target profile that are
    replies, orders by newest first, paginates with OFFSET/LIMIT, enriches via
    the viewer overlay.

    Final ORDER BY created_at DESC applied after enrichment.
*/
CREATE OR REPLACE FUNCTION fetch_feed_profile_replies (
    p_profile_id UUID,
    p_viewer_id UUID,
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
    post_entities             JSONB,
    author_id                 UUID,
    author_username           VARCHAR(255),
    author_name               VARCHAR(50),
    author_image_url          VARCHAR(255),
    post_rel_liked            BOOLEAN,
    post_rel_shared           BOOLEAN,
    author_rel_is_self        BOOLEAN,
    author_rel_following      BOOLEAN,
    author_rel_followed_by    BOOLEAN
)
AS
$$
    WITH profile_replies AS (
        SELECT
            p.id
        FROM posts p
        WHERE p.author_id = p_profile_id
        AND p.parent_id IS NOT NULL
        ORDER BY p.created_at DESC
        OFFSET p_offset
        LIMIT p_limit
    )
    SELECT
        pwc.*
    FROM posts_with_context_and_viewer_v1 (
        ARRAY(SELECT pr.id FROM profile_replies pr),
        p_viewer_id
    ) pwc
    ORDER BY pwc.created_at DESC
$$
LANGUAGE SQL