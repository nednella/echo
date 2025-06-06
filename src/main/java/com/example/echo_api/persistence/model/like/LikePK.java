package com.example.echo_api.persistence.model.like;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ID class representing a composite primary key for {@link Like} entity.
 */
@Getter
@NoArgsConstructor
public class LikePK {

    private UUID postId;

    private UUID authorId;

}
