
/*
 * Function fetches profiles that are followed by the supplied profile ID with the 
 * relevant data required for building a complete Page<SimplifiedProfileDTO> object
 * that the frontend can display to end users.
 *
 */

CREATE OR REPLACE FUNCTION fetch_profile_following(
    p_profile_id UUID,
    p_authenticated_user_id UUID,
    p_offset INTEGER,
    p_limit INTEGER
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
        SELECT
            sp.is_self,
            sp.id,
            sp.username,
            sp.name,
            sp.image_url,
            sp.rel_following,
            sp.rel_followed_by
        FROM (
            SELECT followed_id
            FROM profile_follows
            WHERE follower_id = p_profile_id
            ORDER BY created_at DESC
            OFFSET p_offset
            LIMIT p_limit
        ) f
        CROSS JOIN LATERAL fetch_simplified_profile(f.followed_id, p_authenticated_user_id) sp;
    END;
'
LANGUAGE plpgsql;