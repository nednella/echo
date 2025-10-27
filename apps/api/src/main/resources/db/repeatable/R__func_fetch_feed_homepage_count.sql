DROP FUNCTION IF EXISTS fetch_feed_homepage_count;

CREATE OR REPLACE FUNCTION fetch_feed_homepage_count (
    p_viewer_id UUID
)
RETURNS BIGINT
AS
$$
    SELECT COUNT(*)
    FROM posts p
    LEFT JOIN profile_follows f ON p.author_id = f.followed_id
    WHERE p.parent_id IS NULL
    AND (f.follower_id = p_viewer_id OR p.author_id = p_viewer_id);
$$
LANGUAGE SQL;