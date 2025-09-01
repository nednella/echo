package com.example.echo_api.modules.post.repository;

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

import com.example.echo_api.modules.post.dto.PostDTO;
import com.example.echo_api.modules.post.dto.PostEntitiesDTO;
import com.example.echo_api.modules.post.dto.PostMetricsDTO;
import com.example.echo_api.modules.post.dto.PostRelationshipDTO;
import com.example.echo_api.modules.profile.dto.ProfileRelationshipDTO;
import com.example.echo_api.modules.profile.dto.SimplifiedProfileDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final NamedParameterJdbcTemplate template;

    @Override
    public Optional<PostDTO> findPostDtoById(UUID id, UUID authUserId) {
        String sql = "SELECT * FROM fetch_posts(:post_id, :authenticated_user_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("post_id", new UUID[] { id })
            .addValue("authenticated_user_id", authUserId);

        return template.query(
            sql,
            params,
            new PostDtoRowMapper()).stream().findFirst();
    }

    @Override
    public Page<PostDTO> findRepliesById(UUID id, UUID authUserId, Pageable p) {
        String sql = "SELECT * FROM fetch_post_replies(:post_id, :authenticated_user_id, :offset, :limit)";
        String countSql = "SELECT * FROM fetch_post_replies_count(:post_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("post_id", id)
            .addValue("authenticated_user_id", authUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

        return fetchPaginatedPosts(sql, countSql, params, p);
    }

    @Override
    public Page<PostDTO> findHomepagePosts(UUID authUserId, Pageable p) {
        String sql = "SELECT * FROM fetch_feed_homepage(:authenticated_user_id, :offset, :limit)";
        String countSql = "SELECT * FROM fetch_feed_homepage_count(:authenticated_user_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("authenticated_user_id", authUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

        return fetchPaginatedPosts(sql, countSql, params, p);
    }

    @Override
    public Page<PostDTO> findDiscoverPosts(UUID authUserId, Pageable p) {
        String sql = "SELECT * FROM fetch_feed_discover(:authenticated_user_id, :offset, :limit)";
        String countSql = "SELECT * FROM fetch_feed_discover_count(:authenticated_user_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("authenticated_user_id", authUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

        return fetchPaginatedPosts(sql, countSql, params, p);
    }

    @Override
    public Page<PostDTO> findPostsByProfileId(UUID profileId, UUID authUserId, Pageable p) {
        String sql = "SELECT * FROM fetch_feed_profile(:profile_id, :authenticated_user_id, :offset, :limit)";
        String countSql = "SELECT * FROM fetch_feed_profile_count(:profile_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("profile_id", profileId)
            .addValue("authenticated_user_id", authUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

        return fetchPaginatedPosts(sql, countSql, params, p);
    }

    @Override
    public Page<PostDTO> findRepliesByProfileId(UUID profileId, UUID authUserId, Pageable p) {
        String sql = "SELECT * FROM fetch_feed_profile_replies(:profile_id, :authenticated_user_id, :offset, :limit)";
        String countSql = "SELECT * FROM fetch_feed_profile_replies_count(:profile_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("profile_id", profileId)
            .addValue("authenticated_user_id", authUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

        return fetchPaginatedPosts(sql, countSql, params, p);
    }

    @Override
    public Page<PostDTO> findPostsLikedByProfileId(UUID profileId, UUID authUserId, Pageable p) {
        String sql = "SELECT * FROM fetch_feed_profile_likes(:profile_id, :authenticated_user_id, :offset, :limit)";
        String countSql = "SELECT * FROM fetch_feed_profile_likes_count(:profile_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("profile_id", profileId)
            .addValue("authenticated_user_id", authUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

        return fetchPaginatedPosts(sql, countSql, params, p);
    }

    @Override
    public Page<PostDTO> findPostsMentioningProfileId(UUID profileId, UUID authUserId, Pageable p) {
        String sql = "SELECT * FROM fetch_feed_profile_mentions(:profile_id, :authenticated_user_id, :offset, :limit)";
        String countSql = "SELECT * FROM fetch_feed_profile_mentions_count(:profile_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("profile_id", profileId)
            .addValue("authenticated_user_id", authUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

        return fetchPaginatedPosts(sql, countSql, params, p);
    }

    /**
     * Private method that fetches a paginated list of {@link PostDTO} based on the
     * provided SQL query and parameters.
     * 
     * This method executes the main query to retrieve the posts and a separate
     * count query to determine the total number of available posts, returning a
     * constructed {@link PageImpl}.
     * 
     * @param sql      The query related to fetching the paginated posts.
     * @param countSql The query related to obtaining a total count of available
     *                 posts.
     * @param params
     * @param p        The pagination and sorting configuration.
     * @return A {@link PageImpl} containing the list of {@link PostDTO} objects,
     *         the current page details, and the total number of available posts.
     */
    // @formatter:off
    private Page<PostDTO> fetchPaginatedPosts(
        String sql,
        String countSql,
        MapSqlParameterSource params,
        Pageable p
    ) {
        List<PostDTO> posts = template.query(
            sql,
            params,
            new PostDtoRowMapper());

        Long count = template.queryForObject(
            countSql,
            params,
            Long.class);

        return new PageImpl<>(posts, p, count == null ? 0L : count);
    } // @formatter:on

    /**
     * Maps {@link ResultSet} rows to full {@link PostDTO} objects, including
     * metrics, relationship and entity data where applicable.
     */
    private static class PostDtoRowMapper implements RowMapper<PostDTO> {

        private final ObjectMapper objectMapper;

        public PostDtoRowMapper() {
            this.objectMapper = new ObjectMapper();
        }

        @Override
        public PostDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            boolean isSelf = rs.getBoolean("author_is_self");

            SimplifiedProfileDTO author = new SimplifiedProfileDTO(
                rs.getString("author_id"),
                rs.getString("author_username"),
                rs.getString("author_name"),
                rs.getString("author_image_url"),
                isSelf
                    ? null
                    : new ProfileRelationshipDTO(
                        rs.getBoolean("author_rel_following"),
                        rs.getBoolean("author_rel_followed_by")));

            PostEntitiesDTO entities = null;
            try {
                entities = objectMapper.readValue(rs.getString("post_entities"), PostEntitiesDTO.class);
            } catch (Exception e) {
                throw new SQLException("Failed to parse post_entities JSON", e);
            }

            return new PostDTO(
                rs.getString("id"),
                rs.getString("parent_id"),
                rs.getString("conversation_id"),
                author,
                rs.getString("text"),
                rs.getTimestamp("created_at").toInstant().toString(), // correctly reformats timestamp to ISO-8601
                new PostMetricsDTO(
                    rs.getInt("post_like_count"),
                    rs.getInt("post_reply_count")),
                new PostRelationshipDTO(
                    rs.getBoolean("post_rel_liked")),
                entities);
        }

    }

}
