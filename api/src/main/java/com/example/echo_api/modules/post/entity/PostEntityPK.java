package com.example.echo_api.modules.post.entity;

import java.util.Objects;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ID class representing a composite primary key for {@link PostEntity} entity.
 */
@Getter
@NoArgsConstructor
public class PostEntityPK {

    private UUID postId;

    private PostEntityType type;

    private int start;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PostEntityPK))
            return false;

        PostEntityPK that = (PostEntityPK) o;

        return this.postId.equals(that.postId) &&
            this.type.equals(that.type) &&
            this.start == that.start;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, type, start);
    }

}
