CREATE OR REPLACE FUNCTION profiles_with_context_and_viewer_v1 (
    p_profile_ids UUID[],
    p_viewer_id UUID
)
RETURNS TABLE (
    id                 UUID,
    username           VARCHAR(255),
    name               VARCHAR(50),
    bio                VARCHAR(160),
    location           VARCHAR(30),
    image_url          VARCHAR(255),
    created_at         TIMESTAMPTZ,
    follower_count     BIGINT,
    following_count    BIGINT,
    post_count         BIGINT,
    media_count        BIGINT,
    rel_is_self        BOOLEAN,
    rel_following      BOOLEAN,
    rel_followed_by    BOOLEAN
)
AS
$$
    SELECT
        pwc.*,
        (pwc.id = p_viewer_id) AS rel_is_self,
        EXISTS(SELECT 1 FROM profile_follows pf WHERE pf.follower_id = p_viewer_id AND pf.followed_id = pwc.id) AS rel_following,
        EXISTS(SELECT 1 FROM profile_follows pf WHERE pf.follower_id = pwc.id AND pf.followed_id = p_viewer_id) AS rel_followed_by
    FROM profiles_with_context_v1 pwc
    WHERE pwc.id = ANY(p_profile_ids)
$$
LANGUAGE SQL
