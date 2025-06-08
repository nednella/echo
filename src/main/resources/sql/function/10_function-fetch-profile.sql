
/*
 * Function fetches a single profile by its ID or username with the relevant data
 * required for building a complete ProfileDTO object that the frontend can display
 * to end users.
 *
 * This function should be called with id OR username, with the other being NULL.
 *
*/

CREATE OR REPLACE FUNCTION fetch_profile(
    p_id UUID,
    p_username VARCHAR,
    p_authenticated_user_id UUID
)
RETURNS TABLE (
    is_self            BOOLEAN,
    id                 UUID,
    username           VARCHAR(15),
    name               VARCHAR(50),
    bio                VARCHAR(160),
    location           VARCHAR(30),
    avatar_url         VARCHAR(255),
    banner_url         VARCHAR(255),
    created_at         TIMESTAMPTZ,
    followers_count    BIGINT,
    following_count    BIGINT,
    post_count         BIGINT,
    media_count        BIGINT,
    rel_following      BOOLEAN,
    rel_followed_by    BOOLEAN,
    rel_blocking       BOOLEAN,
    rel_blocked_by     BOOLEAN
)
AS
'
    BEGIN
        RETURN QUERY
        WITH profile_data AS (
            SELECT
                (p.id = p_authenticated_user_id) AS is_self,
                p.id,
                p.username,
                p.name,
                p.bio,
                p.location,
                avatar.transformed_url AS avatar_url,
                banner.transformed_url AS banner_url,
                p.created_at
            FROM profile p
            LEFT JOIN image avatar ON p.avatar_id = avatar.id
            LEFT JOIN image banner ON p.banner_id = banner.id
            WHERE (p_id IS NOT NULL AND p.id = p_id)
               OR (p_username IS NOT NULL AND p.username = p_username)
        ),
        metrics AS (
            SELECT
                (SELECT COUNT(*) FROM follow WHERE followed_id = pd.id) AS followers_count,
                (SELECT COUNT(*) FROM follow WHERE follower_id = pd.id) AS following_count,
                0::BIGINT AS post_count,
                0::BIGINT AS media_count
            FROM profile_data pd
        ),
        relationship AS (
            SELECT
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM follow
                        WHERE follower_id = p_authenticated_user_id
                        AND followed_id = pd.id
                    )
                END AS rel_following,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM follow
                        WHERE follower_id = pd.id
                        AND followed_id = p_authenticated_user_id
                    )
                END AS rel_followed_by,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM block
                        WHERE blocker_id = p_authenticated_user_id
                        AND blocked_id = pd.id
                    )
                END AS rel_blocking,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM block
                        WHERE blocker_id = pd.id
                        AND blocked_id = p_authenticated_user_id
                    )
                END AS rel_blocked_by
            FROM profile_data pd
        )
        SELECT
            pd.is_self,
            pd.id,
            pd.username,
            pd.name,
            pd.bio,
            pd.location,
            pd.avatar_url,
            pd.banner_url,
            pd.created_at,
            m.followers_count,
            m.following_count,
            m.post_count,
            m.media_count,
            r.rel_following,
            r.rel_followed_by,
            r.rel_blocking,
            r.rel_blocked_by
        FROM profile_data pd
        CROSS JOIN metrics m
        CROSS JOIN relationship r;
    END;
'
LANGUAGE plpgsql;