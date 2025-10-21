
/*
 * Function fetches the total count of posts that form a users discover page.
 *
 * To be consumed as part of a pagination implementation where the total number 
 * of entries available are required.
 */

CREATE OR REPLACE FUNCTION fetch_feed_discover_count(
    p_authenticated_user_id UUID
)
RETURNS BIGINT
AS
'
    SELECT COUNT(*)
    FROM post p
    WHERE p.parent_id IS NULL;
'
LANGUAGE sql;