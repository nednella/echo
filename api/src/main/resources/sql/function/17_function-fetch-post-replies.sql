
/*
 * Function fetches replies to a given post by its ID.

 * Posts are ranked by a unique sorting pattern: those with replies from the original
 * author, then by a combination of post engagement metrics, then by creation timestamp.
 * 
*/

CREATE OR REPLACE FUNCTION fetch_post_replies(
    p_post_id UUID,
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
    author_rel_blocking       BOOLEAN,
    author_rel_blocked_by     BOOLEAN,
    post_entities             JSONB
)
AS
'
    BEGIN
        RETURN QUERY
        WITH post_replies AS (
            SELECT 
                p.id,
                p.created_at,
                (SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = p.id) AS like_count,
                (SELECT COUNT(*) FROM post pr WHERE pr.parent_id = p.id) AS reply_count,
                0::BIGINT AS share_count,
                EXISTS (
                    SELECT 1
                    FROM post p2
                    WHERE p2.parent_id = p.id
                    AND p2.author_id = (SELECT p3.author_id FROM post p3 WHERE p3.id = p_post_id)
                ) AS has_original_author_response
            FROM post p
            WHERE p.parent_id = p_post_id
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
            fp.*
        FROM fetch_posts(ARRAY(SELECT sr.id FROM sorted_replies sr), p_authenticated_user_id) fp
        JOIN sorted_replies sr ON fp.id = sr.id
        ORDER BY sr.sort_order;
    END;
'
LANGUAGE plpgsql;