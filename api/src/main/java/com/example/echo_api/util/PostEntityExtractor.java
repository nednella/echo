package com.example.echo_api.util;

import static lombok.AccessLevel.PRIVATE;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.example.echo_api.exception.custom.internalserver.TwitterTextEnumException;
import com.example.echo_api.persistence.model.post.entity.PostEntity;
import com.example.echo_api.persistence.model.post.entity.PostEntityType;
import com.twitter.twittertext.Extractor;

import lombok.NoArgsConstructor;

/**
 * Utility class for handling string parsing using the TwitterText library as
 * part of the post creation process.
 * 
 * <p>
 * For more information, refer to:
 * {@link https://github.com/twitter/twitter-text}.
 */
@NoArgsConstructor(access = PRIVATE)
public class PostEntityExtractor {

    private static final Extractor extractor = new Extractor();

    // Explicitly allow mentions, hashtags & URLs.
    private static final Set<Extractor.Entity.Type> ALLOWED_ENTITY_TYPES = EnumSet.of(
        Extractor.Entity.Type.MENTION, Extractor.Entity.Type.HASHTAG, Extractor.Entity.Type.URL);

    public static List<PostEntity> extract(UUID postId, String text) {
        if (postId == null || text == null) {
            throw new IllegalArgumentException("Extractor arguments cannot be null.");
        }

        List<Extractor.Entity> twEntities = extractor.extractEntitiesWithIndices(text);

        return twEntities
            .stream()
            .filter(e -> ALLOWED_ENTITY_TYPES.contains(e.getType()))
            .map(e -> toPostEntity(postId, e))
            .toList();
    }

    private static PostEntity toPostEntity(UUID postId, Extractor.Entity twEntity) {
        PostEntityType type = null;
        try {
            type = PostEntityType.valueOf(twEntity.getType().name());
        } catch (Exception ex) {
            throw new TwitterTextEnumException(
                "Could not convert TwitterText entity enum type: " + twEntity.getType().name());
        }

        return new PostEntity(
            postId,
            type,
            twEntity.getStart(),
            twEntity.getEnd(),
            twEntity.getValue());
    }

}
