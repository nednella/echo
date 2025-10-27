/* 
    R__func_fetch_post_replies_by_id.sql

    Post Replies fetch: selects direct replies to a post by parent_id, sorts with a
    unique pattern, paginates with OFFSET/LIMIT, enriches via via the viewer overlay.

    Final ORDER BY sort_order applied after enrichment.

    Sort pattern: posts are ranked by those with replies from the original author, then by a
                  combination of post engagement metrics, then by creation timestamp.
*/
DROP FUNCTION IF EXISTS fetch_post_replies_by_id;

CREATE OR REPLACE FUNCTION fetch_post_replies_by_id (
    p_id UUID,
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
    WITH post_replies AS (
            SELECT 
                p.id,
                p.created_at,
                (SELECT COUNT(*) FROM post_likes pl WHERE pl.post_id = p.id) AS like_count,
                (SELECT COUNT(*) FROM posts pr WHERE pr.parent_id = p.id) AS reply_count,
                0::BIGINT AS share_count,
                EXISTS (
                    SELECT 1
                    FROM posts p2
                    WHERE p2.parent_id = p.id
                    AND p2.author_id = (SELECT p3.author_id FROM posts p3 WHERE p3.id = p_id)
                ) AS has_original_author_response
            FROM posts p
            WHERE p.parent_id = p_id
        ),
        sorted_replies AS (
            SELECT
                pr.id,
                ROW_NUMBER() OVER(ORDER BY pr.has_original_author_response DESC,
                                           (pr.like_count + pr.reply_count + pr.share_count) DESC,
                                           pr.created_at DESC) AS sort_order
            FROM post_replies pr
            OFFSET p_offset
            LIMIT p_limit
        )
    SELECT 
        pwc.*
    FROM posts_with_context_and_viewer_v1(ARRAY(SELECT sr.id FROM sorted_replies sr), p_viewer_id) pwc
    JOIN sorted_replies sr ON sr.id = pwc.id
    ORDER BY sr.sort_order
$$
LANGUAGE SQL;