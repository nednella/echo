
/*
 * Function fetches the total count of posts that form a users discover page.
 *
 * To be consumed as part of a pagination implementation where the total number 
 * of entries available are required.
*/

CREATE OR REPLACE FUNCTION fetch_feed_discover_count(
    p_authenticated_user_id UUID
)
RETURNS INTEGER
AS
'
    BEGIN 
        RETURN QUERY
        SELECT COUNT(*)
        FROM post p
        LEFT JOIN block b ON p.author_id = b.blocked_id AND b.blocker_id = p_authenticated_user_id
            WHERE p.parent_id IS NULL
            AND b.blocked_id IS NULL;
    END;
'
LANGUAGE plpgsql;