
/*
 * Function fetches the total count of replies to a given post by its ID.
 *
 * To be consumed as part of a pagination implementation where the total number 
 * of entries available are required.
*/

CREATE OR REPLACE FUNCTION fetch_post_replies_count(
    p_post_id UUID
)
RETURNS BIGINT
AS
'
    SELECT COUNT(*)
    FROM post p
    WHERE p.parent_id = p_post_id;
'
LANGUAGE sql;