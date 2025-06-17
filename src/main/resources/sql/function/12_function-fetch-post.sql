
/*
 * Function fetches a single post by its ID along with the relevant data required
 * for building a complete PostDTO object that the frontend can display to end users.
 * 
*/

CREATE OR REPLACE FUNCTION fetch_post(
    p_post_id UUID,
    p_authenticated_user_id UUID
)
RETURNS TABLE (
    id                        UUID,
    parent_id                 UUID,
    repost_id                 UUID,
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
    author_rel_blocked_by     BOOLEAN,
    post_entities             JSONB,
    is_quoted_repost          BOOLEAN,
    original_post             JSONB
)
AS
'
    BEGIN
        RETURN QUERY
        WITH post_data AS (
            SELECT *
            FROM post p
            WHERE p.id = p_post_id
        ),
        post_metrics AS (
            SELECT
                (SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = pd.id) AS post_like_count,
                (SELECT COUNT(*) FROM post p WHERE p.parent_id = pd.id) AS post_reply_count,
                0::BIGINT AS post_share_count
            FROM post_data pd
        ),
        post_relationship AS (
            SELECT
                EXISTS(
                    SELECT 1 FROM post_like pl
                    WHERE pl.post_id = pd.id
                    AND pl.author_id = p_authenticated_user_id
                ) AS post_rel_liked
            FROM post_data pd
        ),
        author_data AS (
            SELECT 
                sp.is_self AS author_is_self,
                sp.id AS author_id,
                sp.username AS author_username,
                sp.name AS author_name,
                sp.avatar_url AS author_avatar_url,
                sp.rel_following AS author_rel_following,
                sp.rel_followed_by AS author_rel_followed_by,
                sp.rel_blocking AS author_rel_blocking,
                sp.rel_blocked_by AS author_rel_blocked_by
            FROM post_data pd
            CROSS JOIN LATERAL fetch_simplified_profile(pd.author_id, p_authenticated_user_id) sp
        ),
        original_post AS (
            SELECT
            FROM post_data pd
            CROSS JOIN LATERAL fetch_
        )
        SELECT
            pd.id,
            pd.parent_id,
            pd.conversation_id,
            pd.text,
            pd.created_at,
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
            ad.author_rel_blocked_by,
            fetch_post_entities(pd.id) AS post_entities
        FROM post_data pd
        CROSS JOIN post_metrics pm
        CROSS JOIN post_relationship pr
        CROSS JOIN author_data ad;
    END;
'
LANGUAGE plpgsql;