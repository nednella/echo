CREATE OR REPLACE VIEW posts_with_context_v1 AS
SELECT
    p.id,
    p.parent_id,
    p.conversation_id,
    p.text,
    p.created_at,
    lc.post_like_count,
    rc.post_reply_count,
    0::bigint AS post_share_count,-- TODO
    pe.post_entities,
    a.id AS author_id,
    a.username AS author_username,
    a.name AS author_name,
    a.image_url AS author_image_url
FROM posts p

INNER JOIN profiles a -- POST AUTHOR
    ON a.id = p.author_id

LEFT JOIN LATERAL ( -- POST LIKE COUNT
    SELECT COUNT(*) AS post_like_count
    FROM post_likes pl
    WHERE pl.post_id = p.id
) lc ON TRUE

LEFT JOIN LATERAL ( -- POST REPLY COUNT
    SELECT COUNT(*) AS post_reply_count
    FROM posts r
    WHERE r.parent_id = p.id
) AS rc ON TRUE

LEFT JOIN LATERAL ( -- POST ENTITIES
    SELECT
        jsonb_build_object(
            'hashtags', COALESCE(
                jsonb_agg(
                    jsonb_build_object(
                        'start', pe.start_index,
                        'end', pe.end_index,
                        'text', pe.text 
                    )
                    ORDER BY pe.start_index
                )
                FILTER (WHERE pe.entity_type = 'HASHTAG')
            , '[]'::jsonb),
            'mentions', COALESCE(
                jsonb_agg(
                    jsonb_build_object(
                        'start', pe.start_index,
                        'end', pe.end_index,
                        'text', pe.text
                    )
                    ORDER BY pe.start_index
                )
                FILTER (WHERE pe.entity_type = 'MENTION')
            , '[]'::jsonb),
            'urls', COALESCE(
                jsonb_agg(
                    jsonb_build_object(
                        'start', pe.start_index,
                        'end', pe.end_index,
                        'text', pe.text
                    )
                    ORDER BY pe.start_index
                )
                FILTER (WHERE pe.entity_type = 'URL')
            ,'[]'::jsonb)
        ) AS post_entities
    FROM post_entities pe
    WHERE pe.post_id = p.id
) AS pe ON TRUE
