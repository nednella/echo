
/*
 * Function fetches post entities by post ID and returns a JSON object containing arrays
 * of any matches.
 
  replies to a post by its ID along with the relevant data required
 * for building complete PostDTO objects that the frontend can display to end users.

 * Posts are ranked by a unique sorting pattern: those with replies from the original
 * author, then by a combination of post engagement metrics, then by creation timestamp.
 * 
 */

CREATE OR REPLACE FUNCTION fetch_post_entities(
    p_post_id UUID
)
RETURNS TABLE(
    entities    JSONB
)
AS
'
    BEGIN
        RETURN QUERY
        SELECT jsonb_build_object(
            ''hashtags'', COALESCE((
                SELECT jsonb_agg(jsonb_build_object(
                    ''start'', pe.start_index,
                    ''end'', pe.end_index,
                    ''text'', pe.text
                )) AS hashtags
                FROM post_entity pe
                WHERE pe.post_id = p_post_id
                AND pe.entity_type = ''HASHTAG''
            ), ''[]''::jsonb),
            ''mentions'', COALESCE((
                SELECT jsonb_agg(jsonb_build_object(
                    ''start'', pe.start_index,
                    ''end'', pe.end_index,
                    ''text'', pe.text
                )) AS mentions
                FROM post_entity pe
                WHERE pe.post_id = p_post_id
                AND pe.entity_type = ''MENTION''
            ), ''[]''::jsonb),
            ''urls'', COALESCE((
                SELECT jsonb_agg(jsonb_build_object(
                    ''start'', pe.start_index,
                    ''end'', pe.end_index,
                    ''text'', pe.text
                )) AS urls
                FROM post_entity pe
                WHERE pe.post_id = p_post_id
                AND pe.entity_type = ''URL''
            ), ''[]''::jsonb)
        );
    END;
'
LANGUAGE plpgsql;