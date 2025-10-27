DROP FUNCTION IF EXISTS fetch_feed_profile_likes_count;

CREATE OR REPLACE FUNCTION fetch_feed_profile_likes_count (
    p_profile_id UUID
)
RETURNS BIGINT
AS
$$
    SELECT COUNT(*)
    FROM posts p
    INNER JOIN post_likes pl ON p.id = pl.post_id AND pl.author_id = p_profile_id;
$$
LANGUAGE SQL;