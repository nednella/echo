
/*
 * Function fetches profiles that follow the supplied profile ID with the relevant data
 * required for building a complete Page<SimplifiedProfileDTO> object that the frontend
 * can display to end users.
 *
*/

CREATE OR REPLACE FUNCTION fetch_profile_followers(
    p_id UUID,
    p_authenticated_user_id UUID,
    p_offset INTEGER,
    p_limit INTEGER
)
RETURNS TABLE (
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
        SELECT
            sp.id,
            sp.username,
            sp.name,
            sp.avatar_url,
            sp.rel_following,
            sp.rel_followed_by,
            sp.rel_blocking,
            sp.rel_blocked_by
        FROM (
            SELECT follower_id
            FROM follow
            WHERE followed_id = p_id
            ORDER BY created_at DESC
            OFFSET p_offset
            LIMIT p_limit
        ) f
        CROSS JOIN LATERAL fetch_simplified_profile(f.follower_id, p_authenticated_user_id) sp;
    END;
'
LANGUAGE plpgsql;