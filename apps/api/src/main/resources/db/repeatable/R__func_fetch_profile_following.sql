CREATE OR REPLACE FUNCTION fetch_profile_following (
    p_profile_id UUID,
    p_viewer_id UUID,
    p_offset INTEGER,
    p_limit INTEGER
)
RETURNS TABLE (
    id                 UUID,
    username           VARCHAR(255),
    name               VARCHAR(50),
    image_url          VARCHAR(255),
    rel_is_self        BOOLEAN,
    rel_following      BOOLEAN,
    rel_followed_by    BOOLEAN
)
AS
$$
    WITH following AS (
        SELECT followed_id AS id, created_at
        FROM profile_follows
        WHERE follower_id = p_profile_id
        ORDER BY created_at DESC
        OFFSET p_offset
        LIMIT p_limit
    ),
    order AS (
        SELECT
            f.id,
            row_number() OVER (ORDER BY f.created_at DESC) AS order
        FROM following f
    )
    SELECT
        pwc.id,
        pwc.username,
        pwc.name,
        pwc.image_url,
        pwc.rel_is_self,
        pwc.rel_following,
        pwc.rel_followed_by
    FROM profiles_with_context_and_viewer_v1 (
        ARRAY(SELECT f.id FROM following f),
        p_viewer_id
    ) pwc
    JOIN order USING (id)
    ORDER BY order
$$
LANGUAGE SQL