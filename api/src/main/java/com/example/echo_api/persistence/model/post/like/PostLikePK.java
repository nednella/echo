package com.example.echo_api.persistence.model.post.like;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof PostLikePK))
            return false;

        PostLikePK that = (PostLikePK) o;

        return this.postId.equals(that.postId) &&
            this.authorId.equals(that.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, authorId);
    }

}
