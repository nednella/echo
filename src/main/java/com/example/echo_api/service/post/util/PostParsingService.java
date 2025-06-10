package com.example.echo_api.service.post.util;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.echo_api.persistence.model.post.entity.PostEntity;

public interface PostParsingService {

    /**
     * Parse a {@link String} to check for username mentions or hashtags using REGEX
     * pattern matching, intantiating and returning {@link PostEntity} objects for
     * each match.
     * 
     * @param postId The id of the post to be parsed.
     * @param text   The string of text to be parsed.
     * @return A {@link Map} of {@link List} containing {@link PostEntity}
     *         representing any matches in the string. If no matches for a given
     *         type, then it's corresponding list will be empty.
     */
    public Map<String, List<PostEntity>> parse(UUID postId, String text);

}
