DROP FUNCTION IF EXISTS fetch_feed_profile_posts_count;

CREATE OR REPLACE FUNCTION fetch_feed_profile_posts_count (
    p_profile_id UUID
)
RETURNS BIGINT
AS
$$
    SELECT COUNT(*)
    FROM posts p
    WHERE p.author_id = p_profile_id
    AND p.parent_id IS NULL;
$$
LANGUAGE SQL;