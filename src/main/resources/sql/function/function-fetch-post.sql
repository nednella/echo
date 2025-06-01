
/*
 * Function fetches a single post by its ID along with the relevant data required
 * for building a complete PostDTO object that the frontend can display to end users.
 * 
*/

CREATE OR REPLACE FUNCTION fetch_post(post_id UUID, authenticated_user_id UUID)
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
        WITH post_data AS (
            SELECT *
            FROM post p
            WHERE p.id = post_id
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
                    AND pl.author_id = authenticated_user_id
                ) AS post_rel_liked
            FROM post_data pd
        ),
        author_data AS (
            SELECT 
                (p.id = authenticated_user_id) AS author_is_self,
                p.id AS author_id,
                p.username  AS author_username,
                p.name  AS author_name,
                avatar.transformed_url AS author_avatar_url
            FROM post_data pd
            LEFT JOIN profile p ON pd.author_id = p.id
            LEFT JOIN image avatar ON p.avatar_id = avatar.id
        ),
        author_relationship AS (
            SELECT
                CASE WHEN ad.author_is_self THEN NULL
                    ELSE EXISTS (
                        SELECT 1 FROM follow 
                        WHERE follower_id = authenticated_user_id
                        AND followed_id = ad.author_id
                    )
                END AS author_rel_following,
                CASE WHEN ad.author_is_self THEN NULL
                    ELSE EXISTS (
                        SELECT 1 FROM follow 
                        WHERE follower_id = ad.author_id
                        AND followed_id = authenticated_user_id
                    )
                END AS author_rel_followed_by,
                CASE WHEN ad.author_is_self THEN NULL
                    ELSE EXISTS (
                        SELECT 1 FROM block 
                        WHERE blocker_id = authenticated_user_id
                        AND blocked_id = ad.author_id
                    )
                END AS author_rel_blocking,
                CASE WHEN ad.author_is_self THEN NULL
                    ELSE EXISTS (
                        SELECT 1 FROM block 
                        WHERE blocker_id = ad.author_id
                        AND blocked_id = authenticated_user_id
                    )
                END AS author_rel_blocked_by
            FROM author_data ad
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
            ar.author_rel_following,
            ar.author_rel_followed_by,
            ar.author_rel_blocking,
            ar.author_rel_blocked_by
        FROM post_data pd
        CROSS JOIN post_metrics pm
        CROSS JOIN post_relationship pr
        CROSS JOIN author_data ad
        CROSS JOIN author_relationship ar;
    END;
'
LANGUAGE plpgsql;