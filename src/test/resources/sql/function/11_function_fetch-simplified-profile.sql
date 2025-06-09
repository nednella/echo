
/*
 * Function fetches a single profile by its ID with the relevant data required for
 * building a complete SimplifiedProfileDTO object that the frontend can display
 * to end users.
 *
*/

CREATE OR REPLACE FUNCTION fetch_simplified_profile(
    p_id UUID,
    p_authenticated_user_id UUID
)
RETURNS TABLE (
    is_self            BOOLEAN,
    id                 UUID,
    username           VARCHAR(15),
    name               VARCHAR(50),
    avatar_url         VARCHAR(255),
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
                avatar.transformed_url AS avatar_url
            FROM profile p
            LEFT JOIN image avatar ON p.avatar_id = avatar.id
            WHERE p.id = p_id
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
            pd.avatar_url,
            r.rel_following,
            r.rel_followed_by,
            r.rel_blocking,
            r.rel_blocked_by
        FROM profile_data pd
        CROSS JOIN relationship r;
    END;
'
LANGUAGE plpgsql;