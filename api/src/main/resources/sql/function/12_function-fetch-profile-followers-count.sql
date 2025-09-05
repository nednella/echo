
/*
 * Function fetches the total count of followers for a given profile by its ID.
 *
 * To be consumed as part of a pagination implementation where the total number 
 * of entries available are required.
*/

CREATE OR REPLACE FUNCTION fetch_profile_followers_count(
    p_profile_id UUID
)
RETURNS BIGINT
AS
'
    SELECT COUNT(*)
    FROM profile_follow f
    WHERE f.followed_id = p_profile_id;
'
LANGUAGE sql;