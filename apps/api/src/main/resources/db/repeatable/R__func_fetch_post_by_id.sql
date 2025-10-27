/* 
    R__func_fetch_post_by_id.sql

    Single-post fetch: returns one post projected through
    posts_with_context_and_viewer_v1 for the given post ID and viewer ID.
*/
DROP FUNCTION IF EXISTS fetch_post_by_id;

CREATE OR REPLACE FUNCTION fetch_post_by_id (
    p_id UUID,
    p_viewer_id UUID
)
RETURNS TABLE (
    id                        UUID,
    parent_id                 UUID,
    conversation_id           UUID,
    text                      VARCHAR(280),
    created_at                TIMESTAMPTZ,
    post_like_count           BIGINT,
    post_reply_count          BIGINT,
    post_share_count          BIGINT,
    post_entities             JSONB,
    author_id                 UUID,
    author_username           VARCHAR(255),
    author_name               VARCHAR(50),
    author_image_url          VARCHAR(255),
    post_rel_liked            BOOLEAN,
    post_rel_shared           BOOLEAN,
    author_rel_is_self        BOOLEAN,
    author_rel_following      BOOLEAN,
    author_rel_followed_by    BOOLEAN
)
AS
$$
    SELECT * FROM posts_with_context_and_viewer_v1(ARRAY[p_id], p_viewer_id)
$$
LANGUAGE SQL;