package com.example.echo_api.util.extractor;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
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

    public static List<PostEntity> extractEntities(UUID postId, String text) {
        List<Extractor.Entity> twEntities = extractor.extractEntitiesWithIndices(text);

        return twEntities
            .stream()
            .map(entity -> toPostEntity(postId, entity))
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
