package com.example.echo_api.modules.profile.entity;

import java.util.Objects;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ID class representing a composite primary key for {@link Follow} entity.
 */
@Getter
@NoArgsConstructor
public class FollowPK {

    private UUID followerId;

    private UUID followedId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof FollowPK))
            return false;

        FollowPK that = (FollowPK) o;

        return this.followerId.equals(that.followerId) &&
            this.followedId.equals(that.followedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, followedId);
    }

}
