DROP FUNCTION IF EXISTS fetch_profile_following_count;

CREATE OR REPLACE FUNCTION fetch_profile_following_count (
    p_profile_id UUID
)
RETURNS BIGINT
AS
$$
    SELECT COUNT(*)
    FROM profile_follows f
    WHERE f.follower_id = p_profile_id;
$$
LANGUAGE SQL;