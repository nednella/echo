DROP FUNCTION IF EXISTS fetch_feed_profile_mentions_count;

CREATE OR REPLACE FUNCTION fetch_feed_profile_mentions_count(
    p_profile_id UUID
)
RETURNS BIGINT
AS
$$
    SELECT COUNT(*)
    FROM posts p
    INNER JOIN post_entities pe ON pe.post_id = p.id AND pe.entity_type = 'MENTION'
    INNER JOIN profiles pr ON pr.id = p_profile_id AND pr.username = pe.text
$$
LANGUAGE SQL;
        