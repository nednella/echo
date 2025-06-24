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

import com.example.echo_api.persistence.dto.response.profile.ProfileMetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileRelationshipDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomProfileRepositoryImpl implements CustomProfileRepository {

    private final NamedParameterJdbcTemplate template;

    // @formatter:off
    @Override
    public Optional<ProfileDTO> findProfileDtoById(@NonNull UUID id, @NonNull UUID authenticatedUserId) {
        String sql = "SELECT * FROM fetch_profile(:id, NULL, :authenticated_user_id)";

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
        String sql = "SELECT * FROM fetch_profile(NULL, :username, :authenticated_user_id)";

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
        String followersSql = "SELECT * FROM fetch_profile_followers(:id, :authenticated_user_id, :offset, :limit)";

        MapSqlParameterSource followersParams = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("authenticated_user_id", authenticatedUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

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
        String followingSql = "SELECT * FROM fetch_profile_following(:id, :authenticated_user_id, :offset, :limit)";
        
        MapSqlParameterSource followingParams = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("authenticated_user_id", authenticatedUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

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
                new ProfileMetricsDTO(
                    rs.getInt("followers_count"),
                    rs.getInt("following_count"),
                    rs.getInt("post_count")),
                isSelf
                    ? null
                    : new ProfileRelationshipDTO(
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
                    : new ProfileRelationshipDTO(
                        rs.getBoolean("rel_following"),
                        rs.getBoolean("rel_followed_by"),
                        rs.getBoolean("rel_blocking"),
                        rs.getBoolean("rel_blocked_by")));
        }

    }

}
