DROP FUNCTION IF EXISTS fetch_post_replies_by_id_count;

CREATE OR REPLACE FUNCTION fetch_post_replies_by_id_count (
    p_id UUID
)
RETURNS BIGINT
AS
$$
    SELECT
        COUNT(*)
    FROM posts p
    WHERE p.parent_id = p_id;
$$
LANGUAGE SQL;