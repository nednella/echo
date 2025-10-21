
/*
 * Function fetches the total count of posts that form a users profile page.
 *
 * To be consumed as part of a pagination implementation where the total number 
 * of entries available are required.
 */

CREATE OR REPLACE FUNCTION fetch_feed_profile_count(
    p_profile_id UUID
)
RETURNS BIGINT
AS
'
    SELECT COUNT(*)
    FROM posts p
    WHERE p.author_id = p_profile_id
    AND p.parent_id IS NULL;
'
LANGUAGE sql;