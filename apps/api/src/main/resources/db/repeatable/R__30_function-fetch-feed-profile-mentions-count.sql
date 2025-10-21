
/*
 * Function fetches the total count of posts that form a users profile mentions tab.
 *
 * To be consumed as part of a pagination implementation where the total number 
 * of entries available are required.
 */

CREATE OR REPLACE FUNCTION fetch_feed_profile_mentions_count(
    p_profile_id UUID
)
RETURNS BIGINT
AS
'
    SELECT COUNT(*)
    FROM posts p
    INNER JOIN post_entities pe ON p.id = pe.post_id AND pe.entity_type = ''MENTION''
    INNER JOIN profiles pr ON pe.text = pr.username AND pr.id = p_profile_id;
'
LANGUAGE sql;