/* 
    R__func_fetch_feed_profile_mentions.sql

    Profile Mentions feed: selects posts containing MENTION entities matching the
    target profile’s username, orders by newest first, paginates with OFFSET/LIMIT,
    enriches via via the viewer overlay.

    Final ORDER BY created_at DESC applied after enrichment.
*/
DROP FUNCTION IF EXISTS fetch_feed_profile_mentions;

CREATE OR REPLACE FUNCTION fetch_feed_profile_mentions (
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
    WITH profile_mentions AS (
        SELECT
            p.id
        FROM posts p
        INNER JOIN post_entities pe ON pe.post_id = p.id AND pe.entity_type = 'MENTION'
        INNER JOIN profiles pr ON pr.id = p_profile_id AND pr.username = pe.text
        ORDER BY p.created_at DESC
        OFFSET p_offset
        LIMIT p_limit
    )
    SELECT
        pwc.*
    FROM posts_with_context_and_viewer_v1 (
        ARRAY(SELECT pm.id FROM profile_mentions pm),
        p_viewer_id
    ) pwc
    ORDER BY pwc.created_at DESC
$$
LANGUAGE SQL;