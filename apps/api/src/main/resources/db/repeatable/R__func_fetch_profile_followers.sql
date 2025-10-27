/* 
    R__func_fetch_profile_followers.sql

    Followers list: selects follower edges for a profile, sorts by newest first,
    paginates with OFFSET/LIMIT, enriches via via the viewer overlay.
    
    Final ORDER BY profile_follows.created_at DESC applied after enrichment.
*/
DROP FUNCTION IF EXISTS fetch_profile_followers;

CREATE OR REPLACE FUNCTION fetch_profile_followers (
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
    WITH followers AS (
        SELECT follower_id AS id, created_at
        FROM profile_follows
        WHERE followed_id = p_profile_id
        ORDER BY created_at DESC
        OFFSET p_offset
        LIMIT p_limit
    ),
    sort_order AS (
        SELECT
            f.id,
            row_number() OVER (ORDER BY f.created_at DESC) AS ord
        FROM followers f
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
        ARRAY(SELECT f.id FROM followers f),
        p_viewer_id
    ) pwc
    JOIN sort_order USING (id)
    ORDER BY ord
$$
LANGUAGE SQL;