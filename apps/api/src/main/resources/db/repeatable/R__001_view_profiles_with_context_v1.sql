CREATE OR REPLACE VIEW profiles_with_context_v1 AS
SELECT
    p.id,
    p.username,
    p.name,
    p.bio,
    p.location,
    p.image_url,
    p.created_at,
    frc.follower_count,
    fgc.following_count,
    pc.post_count,
    0::BIGINT AS media_count
FROM profiles p

LEFT JOIN LATERAL ( -- FOLLOWER COUNT
    SELECT COUNT(*) AS follower_count
    FROM profile_follows pf
    WHERE pf.followed_id = p.id
) AS frc ON TRUE

LEFT JOIN LATERAL ( -- FOLLOWING COUNT
    SELECT COUNT(*) AS following_count
    FROM profile_follows pf
    WHERE pf.follower_id = p.id
) AS fgc ON TRUE

LEFT JOIN LATERAL ( -- POST COUNT
    SELECT COUNT(*) AS post_count
    FROM posts po
    WHERE po.author_id = p.id
) AS pc ON TRUE