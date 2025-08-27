
/**
 * Function fetches posts and respective contextual information for the given post ID(s).
 *
*/

CREATE OR REPLACE FUNCTION fetch_posts(
    p_post_ids UUID[],
    p_authenticated_user_id UUID
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
        WITH post_data AS (
            SELECT *
            FROM post p
            WHERE p.id = ANY(p_post_ids)
        ),
        post_metrics AS (
            SELECT
                pd.id AS post_id,
                (SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = pd.id) AS post_like_count,
                (SELECT COUNT(*) FROM post pr WHERE pr.parent_id = pd.id) AS post_reply_count,
                0::BIGINT AS post_share_count
            FROM post_data pd
        ),
        post_relationship AS (
            SELECT
                pd.id AS post_id,
                EXISTS(
                    SELECT 1 FROM post_like pl
                    WHERE pl.post_id = pd.id
                    AND pl.author_id = p_authenticated_user_id
                ) AS post_rel_liked,
                FALSE::BOOLEAN AS post_rel_shared
            FROM post_data pd
        ),
        author_data AS (
            SELECT
                pd.id AS post_id,
                sp.is_self AS author_is_self,
                sp.id AS author_id,
                sp.username AS author_username,
                sp.name AS author_name,
                sp.image_url AS author_image_url,
                sp.rel_following AS author_rel_following,
                sp.rel_followed_by AS author_rel_followed_by
            FROM post_data pd
            CROSS JOIN LATERAL fetch_simplified_profile(pd.author_id, p_authenticated_user_id) sp
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
            pr.post_rel_shared,
            ad.author_is_self,
            ad.author_id,
            ad.author_username,
            ad.author_name,
            ad.author_image_url,
            ad.author_rel_following,
            ad.author_rel_followed_by,
            fetch_post_entities(pd.id) AS post_entities
        FROM post_data pd
        LEFT JOIN post_metrics pm ON pd.id = pm.post_id
        LEFT JOIN post_relationship pr ON pd.id = pr.post_id
        LEFT JOIN author_data ad ON pd.id = ad.post_id;
    END;
'
LANGUAGE plpgsql;