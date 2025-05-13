package com.example.echo_api.persistence.repository.custom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;

import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomProfileRepositoryImpl implements CustomProfileRepository {

    private final NamedParameterJdbcTemplate template;

    // @formatter:off
    @Override
    public Optional<ProfileDTO> findProfileDtoById(@NonNull UUID id, @NonNull UUID authenticatedUserId) {
        String sql = """
            WITH profile_data AS (
                SELECT
                    (p.id = :authenticated_user_id) AS is_self,
                    p.id,
                    p.username,
                    p.name,
                    p.bio,
                    p.location,
                    avatar.transformed_url AS avatar_url,
                    banner.transformed_url AS banner_url,
                    p.created_at,
                    (SELECT COUNT(*) FROM follow WHERE followed_id = p.id) AS followers,
                    (SELECT COUNT(*) FROM follow WHERE follower_id = p.id) AS following,
                    0 AS posts,
                    0 AS media
                FROM profile p
                LEFT JOIN image avatar ON p.avatar_id = avatar.id
                LEFT JOIN image banner ON p.banner_id = banner.id
                WHERE p.id = :id
            )
            SELECT
                pd.*,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM follow
                            WHERE follower_id = :authenticated_user_id
                            AND followed_id = pd.id
                    )
                END AS rel_following,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM follow
                            WHERE follower_id = pd.id
                            AND followed_id = :authenticated_user_id
                    )
                END AS rel_followed_by,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM block
                            WHERE blocker_id = :authenticated_user_id
                            AND blocked_id = pd.id
                    )
                END AS rel_blocking,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM block
                            WHERE blocker_id = pd.id
                            AND blocked_id = :authenticated_user_id
                    ) 
                END AS rel_blocked_by
            FROM profile_data pd
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("authenticated_user_id", authenticatedUserId);

        return template.query(
            sql,
            params,
            new ProfileDtoRowMapper()
        ).stream().findFirst();
    }
    // @formatter:on

    // @formatter:off
    @Override
    public Optional<ProfileDTO> findProfileDtoByUsername(@NonNull String username, @NonNull UUID authenticatedUserId) {
        String sql = """
            WITH profile_data AS (
                SELECT
                    (p.id = :authenticated_user_id) AS is_self,
                    p.id,
                    p.username,
                    p.name,
                    p.bio,
                    p.location,
                    avatar.transformed_url AS avatar_url,
                    banner.transformed_url AS banner_url,
                    p.created_at,
                    (SELECT COUNT(*) FROM follow WHERE followed_id = p.id) AS followers,
                    (SELECT COUNT(*) FROM follow WHERE follower_id = p.id) AS following,
                    0 AS posts,
                    0 AS media
                FROM profile p
                LEFT JOIN image avatar ON p.avatar_id = avatar.id
                LEFT JOIN image banner ON p.banner_id = banner.id
                WHERE p.username = :username
            )
            SELECT
                pd.*,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM follow
                            WHERE follower_id = :authenticated_user_id
                            AND followed_id = pd.id
                    )
                END AS rel_following,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM follow
                            WHERE follower_id = pd.id
                            AND followed_id = :authenticated_user_id
                    )
                END AS rel_followed_by,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM block
                            WHERE blocker_id = :authenticated_user_id
                            AND blocked_id = pd.id
                    )
                END AS rel_blocking,
                CASE WHEN pd.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM block
                            WHERE blocker_id = pd.id
                            AND blocked_id = :authenticated_user_id
                    ) 
                END AS rel_blocked_by
            FROM profile_data pd
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("username", username)
            .addValue("authenticated_user_id", authenticatedUserId);

        return template.query(
            sql,
            params,
            new ProfileDtoRowMapper()
        ).stream().findFirst();
    }
    // @formatter:on

    // @formatter:off
    @Override
    public Page<SimplifiedProfileDTO> findFollowerDtosById(@NonNull UUID id, @NonNull UUID authenticatedUserId, @NonNull Pageable p) {
        String followersSql = """
            WITH paginated_followers AS (
                SELECT
                    (f.follower_id = :authenticated_user_id) AS is_self,
                    f.follower_id AS id,
                    p.username,
                    p.name,
                    f.created_at,
                    avatar.transformed_url AS avatar_url
                FROM follow f
                LEFT JOIN profile p ON p.id = f.follower_id
                LEFT JOIN image avatar ON p.avatar_id = avatar.id
                WHERE f.followed_id = :id
                ORDER BY f.created_at DESC
                LIMIT :limit
                OFFSET :offset
            )
            SELECT
                pf.*,
                CASE WHEN pf.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM follow
                            WHERE follower_id = :authenticated_user_id
                            AND followed_id = pf.id
                    )
                END AS rel_following,
                CASE WHEN pf.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM follow
                            WHERE follower_id = pf.id
                            AND followed_id = :authenticated_user_id
                    )
                END AS rel_followed_by,
                CASE WHEN pf.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM block
                            WHERE blocker_id = :authenticated_user_id
                            AND blocked_id = pf.id
                    )
                END AS rel_blocking,
                CASE WHEN pf.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM block
                            WHERE blocker_id = pf.id
                            AND blocked_id = :authenticated_user_id
                    ) 
                END AS rel_blocked_by
            FROM paginated_followers pf
        """;

        MapSqlParameterSource followersParams = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("authenticated_user_id", authenticatedUserId)
            .addValue("limit", p.getPageSize())
            .addValue("offset", (int) p.getOffset()
        );

        List<SimplifiedProfileDTO> followers = template.query(
            followersSql,
            followersParams,
            new SimplifiedProfileDtoRowMapper()
        );

        String countSql = "SELECT COUNT(*) FROM follow f WHERE f.followed_id = :id";

        MapSqlParameterSource countParams = new MapSqlParameterSource()
            .addValue("id", id);

        Long count = template.queryForObject(
            countSql,
            countParams,
            Long.class
        );

        return new PageImpl<>(followers, p, count == null ? 0L : count);
    }
    // @formatter:on

    // @formatter:off
    @Override
    public Page<SimplifiedProfileDTO> findFollowingDtosById(@NonNull UUID id, @NonNull UUID authenticatedUserId, @NonNull Pageable p) {
        String followingSql = """
            WITH paginated_following AS (
                SELECT
                    (f.followed_id = :authenticated_user_id) AS is_self,
                    f.followed_id AS id,
                    p.username,
                    p.name,
                    f.created_at,
                    avatar.transformed_url AS avatar_url
                FROM follow f
                LEFT JOIN profile p ON p.id = f.followed_id
                LEFT JOIN image avatar ON p.avatar_id = avatar.id
                WHERE f.follower_id = :id
                ORDER BY f.created_at DESC
                LIMIT :limit
                OFFSET :offset
            )
            SELECT
                pf.*,
                CASE WHEN pf.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM follow
                            WHERE follower_id = :authenticated_user_id
                            AND followed_id = pf.id
                    )
                END AS rel_following,
                CASE WHEN pf.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM follow
                            WHERE follower_id = pf.id
                            AND followed_id = :authenticated_user_id
                    )
                END AS rel_followed_by,
                CASE WHEN pf.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM block
                            WHERE blocker_id = :authenticated_user_id
                            AND blocked_id = pf.id
                    )
                END AS rel_blocking,
                CASE WHEN pf.is_self THEN NULL
                    ELSE EXISTS(
                        SELECT 1 FROM block
                            WHERE blocker_id = pf.id
                            AND blocked_id = :authenticated_user_id
                    ) 
                END AS rel_blocked_by
            FROM paginated_following pf
        """;

        MapSqlParameterSource followingParams = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("authenticated_user_id", authenticatedUserId)
            .addValue("limit", p.getPageSize())
            .addValue("offset", (int) p.getOffset()
        );

        List<SimplifiedProfileDTO> followers = template.query(
            followingSql,
            followingParams,
            new SimplifiedProfileDtoRowMapper()
        );

        String countSql = "SELECT COUNT(*) FROM follow f WHERE f.follower_id = :id";

        MapSqlParameterSource countParams = new MapSqlParameterSource()
            .addValue("id", id);

        Long count = template.queryForObject(
            countSql,
            countParams,
            Long.class
        );

        return new PageImpl<>(followers, p, count == null ? 0L : count);
    }
    // @formatter:on

    /**
     * Maps {@link ResultSet} rows to full {@link ProfileDTO} objects, including
     * metrics and relationship data where applicable.
     */
    private static class ProfileDtoRowMapper implements RowMapper<ProfileDTO> {

        @Override
        public ProfileDTO mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            boolean isSelf = rs.getBoolean("is_self");

            return new ProfileDTO(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("name"),
                rs.getString("bio"),
                rs.getString("location"),
                rs.getString("avatar_url"),
                rs.getString("banner_url"),
                rs.getTimestamp("created_at").toInstant().toString(),
                new MetricsDTO(
                    rs.getInt("followers"),
                    rs.getInt("following"),
                    rs.getInt("posts"),
                    rs.getInt("media")),
                isSelf
                    ? null
                    : new RelationshipDTO(
                        rs.getBoolean("rel_following"),
                        rs.getBoolean("rel_followed_by"),
                        rs.getBoolean("rel_blocking"),
                        rs.getBoolean("rel_blocked_by")));
        }

    }

    /**
     * Maps {@link ResultSet} rows to {@link SimplifiedProfileDTO} for nested
     * contexts like posts or comments.
     */
    private static class SimplifiedProfileDtoRowMapper implements RowMapper<SimplifiedProfileDTO> {

        @Override
        public SimplifiedProfileDTO mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            boolean isSelf = rs.getBoolean("is_self");

            return new SimplifiedProfileDTO(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("name"),
                rs.getString("avatar_url"),
                isSelf
                    ? null
                    : new RelationshipDTO(
                        rs.getBoolean("rel_following"),
                        rs.getBoolean("rel_followed_by"),
                        rs.getBoolean("rel_blocking"),
                        rs.getBoolean("rel_blocked_by")));
        }

    }

}
