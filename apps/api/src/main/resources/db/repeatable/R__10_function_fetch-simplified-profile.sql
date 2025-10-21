
/*
 * Function fetches a single profile by its ID with the relevant data required for
 * building a complete SimplifiedProfileDTO object that the frontend can display
 * to end users.
 *
 */

CREATE OR REPLACE FUNCTION fetch_simplified_profile(
    p_profile_id UUID,
    p_authenticated_user_id UUID
)
RETURNS TABLE (
    is_self            BOOLEAN,
    id                 UUID,
    username           VARCHAR(255),
    name               VARCHAR(50),
    image_url          VARCHAR(255),
    rel_following      BOOLEAN,
    rel_followed_by    BOOLEAN
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
                p.image_url
            FROM profiles p
            WHERE p.id = p_profile_id
        ),
        relationship AS (
            SELECT
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM profile_follows
                        WHERE follower_id = p_authenticated_user_id
                        AND followed_id = pd.id
                    )
                END AS rel_following,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM profile_follows
                        WHERE follower_id = pd.id
                        AND followed_id = p_authenticated_user_id
                    )
                END AS rel_followed_by
            FROM profile_data pd
        )
        SELECT
            pd.is_self,
            pd.id,
            pd.username,
            pd.name,
            pd.image_url,
            r.rel_following,
            r.rel_followed_by
        FROM profile_data pd
        CROSS JOIN relationship r;
    END;
'
LANGUAGE plpgsql;