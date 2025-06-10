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

import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.persistence.dto.response.post.PostEntitiesDTO;
import com.example.echo_api.persistence.dto.response.post.PostMetricsDTO;
import com.example.echo_api.persistence.dto.response.post.PostRelationshipDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileRelationshipDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final NamedParameterJdbcTemplate template;

    // @formatter:off
    @Override
    public Optional<PostDTO> findPostDtoById(@NonNull UUID id, @NonNull UUID authenticatedUserId) {
        String sql = "SELECT * FROM fetch_post(:post_id, :authenticated_user_id)";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("post_id", id)
            .addValue("authenticated_user_id", authenticatedUserId);

        return template.query(
            sql,
            params,
            new PostDtoRowMapper()
        ).stream().findFirst();
    }
    // @formatter:on

    // @formatter:off
    @Override
    public Page<PostDTO> findReplyDtosById(@NonNull UUID id, @NonNull UUID authenticatedUserId, @NonNull Pageable p) {
        String repliesSql = "SELECT * FROM fetch_post_replies(:post_id, :authenticated_user_id, :offset, :limit)";

        MapSqlParameterSource repliesParams = new MapSqlParameterSource()
            .addValue("post_id", id)
            .addValue("authenticated_user_id", authenticatedUserId)
            .addValue("offset", (int) p.getOffset())
            .addValue("limit", p.getPageSize());

        List<PostDTO> replies = template.query(
            repliesSql,
            repliesParams,
            new PostDtoRowMapper()
        );

        String countSql = "SELECT COUNT(*) FROM post p WHERE p.parent_id = :post_id";

        MapSqlParameterSource countParams = new MapSqlParameterSource()
            .addValue("post_id", id);

        Long count = template.queryForObject(
            countSql,
            countParams,
            Long.class
        );

        return new PageImpl<>(replies, p, count == null ? 0L : count);
    }
    // @formatter:on

    private static class PostDtoRowMapper implements RowMapper<PostDTO> {

        private final ObjectMapper objectMapper;

        public PostDtoRowMapper() {
            this.objectMapper = new ObjectMapper();
        }

        @Override
        public PostDTO mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            boolean isSelf = rs.getBoolean("author_is_self");

            SimplifiedProfileDTO author = new SimplifiedProfileDTO(
                rs.getString("author_id"),
                rs.getString("author_username"),
                rs.getString("author_name"),
                rs.getString("author_avatar_url"),
                isSelf
                    ? null
                    : new ProfileRelationshipDTO(
                        rs.getBoolean("author_rel_following"),
                        rs.getBoolean("author_rel_followed_by"),
                        rs.getBoolean("author_rel_blocking"),
                        rs.getBoolean("author_rel_blocked_by")));

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
                rs.getString("created_at"),
                new PostMetricsDTO(
                    rs.getInt("post_like_count"),
                    rs.getInt("post_reply_count"),
                    rs.getInt("post_share_count")),
                new PostRelationshipDTO(
                    rs.getBoolean("post_rel_liked")),
                entities);
        }

    }

}
