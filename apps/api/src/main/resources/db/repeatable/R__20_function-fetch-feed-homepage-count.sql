
/*
 * Function fetches the total count of posts that form a users homepage.
 *
 * To be consumed as part of a pagination implementation where the total number 
 * of entries available are required.
 */

CREATE OR REPLACE FUNCTION fetch_feed_homepage_count(
    p_authenticated_user_id UUID
)
RETURNS BIGINT
AS
'
    SELECT COUNT(*)
    FROM posts p
    LEFT JOIN profile_follows f ON p.author_id = f.followed_id
    WHERE p.parent_id IS NULL
    AND (f.follower_id = p_authenticated_user_id
        OR p.author_id = p_authenticated_user_id);
'
LANGUAGE sql;