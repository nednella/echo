
/*
 * Function fetches replies to a post by its ID along with the relevant data required
 * for building complete PostDTO objects that the frontend can display to end users.

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
    text                      VARCHAR(140),
    created_at                TIMESTAMPTZ,
    post_like_count           BIGINT,
    post_reply_count          BIGINT,
    post_share_count          BIGINT,
    post_rel_liked            BOOLEAN,
    author_is_self            BOOLEAN,
    author_id                 UUID,
    author_username           VARCHAR(15),
    author_name               VARCHAR(50),
    author_avatar_url         VARCHAR(255),
    author_rel_following      BOOLEAN,
    author_rel_followed_by    BOOLEAN,
    author_rel_blocking       BOOLEAN,
    author_rel_blocked_by     BOOLEAN
)
AS
'
    BEGIN
        RETURN QUERY
        WITH post_replies AS (
            SELECT 
                p.*,
                (SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = p.id) AS likes,
                (SELECT COUNT(*) FROM post pr WHERE pr.parent_id = p.id) AS replies,
                0::BIGINT AS shares,
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
                pr.*,
                (pr.likes + pr.replies + pr.shares) AS engagement
            FROM post_replies pr
            ORDER BY pr.has_original_author_response DESC, engagement DESC, pr.created_at DESC
            OFFSET p_offset
            LIMIT p_limit
        ),
        post_metrics AS (
            SELECT
                sr.id AS post_id,
                sr.likes AS post_like_count,
                sr.replies AS post_reply_count,
                sr.shares AS post_share_count
            FROM sorted_replies sr
        ),
        post_relationship AS (
            SELECT
                sr.id AS post_id,
                EXISTS(
                    SELECT 1 FROM post_like pl
                    WHERE pl.post_id = sr.id
                    AND pl.author_id = p_authenticated_user_id
                ) AS post_rel_liked
            FROM sorted_replies sr
        ),
        author_data AS (
            SELECT
                sr.id AS post_id,
                sp.is_self AS author_is_self,
                sp.id AS author_id,
                sp.username AS author_username,
                sp.name AS author_name,
                sp.avatar_url AS author_avatar_url,
                sp.rel_following AS author_rel_following,
                sp.rel_followed_by AS author_rel_followed_by,
                sp.rel_blocking AS author_rel_blocking,
                sp.rel_blocked_by AS author_rel_blocked_by
            FROM sorted_replies sr
            CROSS JOIN LATERAL fetch_simplified_profile(sr.author_id, p_authenticated_user_id) sp
        )
        SELECT
            sr.id,
            sr.parent_id,
            sr.conversation_id,
            sr.text,
            sr.created_at,
            pm.post_like_count,
            pm.post_reply_count,
            pm.post_share_count,
            pr.post_rel_liked,
            ad.author_is_self,
            ad.author_id,
            ad.author_username,
            ad.author_name,
            ad.author_avatar_url,
            ad.author_rel_following,
            ad.author_rel_followed_by,
            ad.author_rel_blocking,
            ad.author_rel_blocked_by
        FROM sorted_replies sr
        LEFT JOIN post_metrics pm ON sr.id = pm.post_id
        LEFT JOIN post_relationship pr ON sr.id = pr.post_id
        LEFT JOIN author_data ad ON sr.id = ad.post_id;
    END;
'
LANGUAGE plpgsql;