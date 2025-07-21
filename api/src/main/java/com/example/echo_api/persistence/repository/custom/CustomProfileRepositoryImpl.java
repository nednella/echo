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

import com.example.echo_api.persistence.dto.response.profile.ProfileMetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileRelationshipDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomProfileRepositoryImpl implements CustomProfileRepository {

    private final NamedParameterJdbcTemplate template;

    @Override
    public Optional<ProfileDTO> findProfileDtoById(UUID id, UUID authUserId) {
        String sql = "SELECT * FROM fetch_profile(:id, NULL, :authenticated_user_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("authenticated_user_id", authUserId);

        return template.query(
            sql,
            params,
            new ProfileDtoRowMapper()).stream().findFirst();
    }

    @Override
    public Optional<ProfileDTO> findProfileDtoByUsername(String username, UUID authUserId) {
        String sql = "SELECT * FROM fetch_profile(NULL, :username, :authenticated_user_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("username", username)
            .addValue("authenticated_user_id", authUserId);

        return template.query(
            sql,
            params,
            new ProfileDtoRowMapper()).stream().findFirst();
    }

    @Override
    public Page<SimplifiedProfileDTO> findFollowerDtosById(UUID id, UUID authUserId, Pageable p) {
        String sql = "SELECT * FROM fetch_profile_followers(:profile_id, :authenticated_user_id, :offset, :limit)";
        String countSql = "SELECT * FROM fetch_profile_followers_count(:profile_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("profile_id", id)
            .addValue("authenticated_user_id", authUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

        return fetchPaginatedProfiles(sql, countSql, params, p);
    }

    @Override
    public Page<SimplifiedProfileDTO> findFollowingDtosById(UUID id, UUID authUserId, Pageable p) {
        String sql = "SELECT * FROM fetch_profile_following(:profile_id, :authenticated_user_id, :offset, :limit)";
        String countSql = "SELECT * FROM fetch_profile_following_count(:profile_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("profile_id", id)
            .addValue("authenticated_user_id", authUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

        return fetchPaginatedProfiles(sql, countSql, params, p);
    }

    /**
     * Private method that fetches a paginated list of {@link SimplifiedProfileDTO}
     * based on the provided SQL query and parameters.
     * 
     * This method executes the main query to retrieve the profiles and a separate
     * count query to determine the total number of available profiles, returning a
     * constructed {@link PageImpl}.
     * 
     * @param sql      The query related to fetching the paginated profiles.
     * @param countSql The query related to obtaining a total count of available
     *                 profiles.
     * @param params
     * @param p        The pagination and sorting configuration.
     * @return A {@link PageImpl} containing the list of
     *         {@link SimplifiedProfileDTO} objects, the current page details, and
     *         the total number of available profiles.
     */
    // @formatter:off
    private Page<SimplifiedProfileDTO> fetchPaginatedProfiles(
        String sql,
        String countSql,
        MapSqlParameterSource params,
        Pageable p
    ) {
        List<SimplifiedProfileDTO> profiles = template.query(
            sql,
            params,
            new SimplifiedProfileDtoRowMapper());

        Long count = template.queryForObject(
            countSql,
            params,
            Long.class);

        return new PageImpl<>(profiles, p, count == null ? 0L : count);
    } // @formatter:on

    /**
     * Maps {@link ResultSet} rows to full {@link ProfileDTO} objects, including
     * metrics and relationship data where applicable.
     */
    private static class ProfileDtoRowMapper implements RowMapper<ProfileDTO> {

        @Override
        public ProfileDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            boolean isSelf = rs.getBoolean("is_self");

            return new ProfileDTO(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("name"),
                rs.getString("bio"),
                rs.getString("location"),
                rs.getString("avatar_url"),
                rs.getString("banner_url"),
                rs.getTimestamp("created_at").toInstant().toString(), // correctly reformats timestamp to ISO-8601
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
        public SimplifiedProfileDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
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
