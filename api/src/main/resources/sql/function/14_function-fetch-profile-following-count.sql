
/*
 * Function fetches the total count of following for a given profile by its ID.
 *
 * To be consumed as part of a pagination implementation where the total number 
 * of entries available are required.
*/

CREATE OR REPLACE FUNCTION fetch_profile_following_count(
    p_profile_id UUID
)
RETURNS BIGINT
AS
'
    SELECT COUNT(*)
    FROM follow f
    WHERE f.follower_id = p_profile_id;
'
LANGUAGE sql;