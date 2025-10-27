DROP FUNCTION IF EXISTS fetch_feed_discover_count;

CREATE OR REPLACE FUNCTION fetch_feed_discover_count ()
RETURNS BIGINT
AS
$$
    SELECT 
        COUNT(*)
    FROM posts
    WHERE parent_id IS NULL;
$$
LANGUAGE SQL;