package com.example.echo_api.persistence.model.post.like;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ID class representing a composite primary key for {@link PostLike} entity.
 */
@Getter
@NoArgsConstructor
public class PostLikePK {

    private UUID postId;

    private UUID authorId;

}
