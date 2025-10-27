/* 
    R__func_fetch_profile_by_id.sql

    Single-profile fetch by ID: returns one profile projected through
    profiles_with_context_and_viewer_v1 for the given profile ID and viewer ID.
*/
DROP FUNCTION IF EXISTS fetch_profile_by_id;

CREATE OR REPLACE FUNCTION fetch_profile_by_id (
    p_id UUID,
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
    SELECT * FROM profiles_with_context_and_viewer_v1(ARRAY[p_id], p_viewer_id)
$$
LANGUAGE SQL;