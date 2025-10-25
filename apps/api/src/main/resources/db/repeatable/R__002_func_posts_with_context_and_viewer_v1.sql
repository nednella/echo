CREATE OR REPLACE FUNCTION posts_with_context_and_viewer_v1 (
    p_post_ids UUID[],
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
    post_rel_shared           BOOLEAN, -- TODO
    author_rel_is_self        BOOLEAN,
    author_rel_following      BOOLEAN,
    author_rel_followed_by    BOOLEAN
)
AS
$$
    SELECT
        pwc.*,
        EXISTS(SELECT 1 FROM post_likes pl WHERE pl.post_id = pwc.id AND pl.author_id = p_viewer_id)    AS post_rel_liked,
        FALSE                                                                                           AS post_rel_shared,
        (pwc.author_id = p_viewer_id)                                                                   AS author_rel_is_self,
        EXISTS(SELECT 1 FROM profile_follows pf WHERE pf.follower_id = p_viewer_id AND pf.followed_id = pwc.author_id) AS author_rel_following,
        EXISTS(SELECT 1 FROM profile_follows pf WHERE pf.follower_id = pwc.author_id AND pf.followed_id = p_viewer_id) AS author_rel_followed_by
    FROM posts_with_context_v1 pwc
    WHERE pwc.id = ANY(p_post_ids)
$$
LANGUAGE SQL