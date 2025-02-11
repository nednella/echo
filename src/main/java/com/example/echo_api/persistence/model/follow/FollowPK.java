package com.example.echo_api.persistence.model.follow;

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

    private UUID followingId;

}
