package com.example.echo_api.persistence.model.post.entity;

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

    private int start;

}
