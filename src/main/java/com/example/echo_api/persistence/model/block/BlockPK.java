package com.example.echo_api.persistence.model.block;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ID class representing a composite primary key for {@link Block} entity.
 */
@Getter
@NoArgsConstructor
public class BlockPK {

    private UUID blockerId;

    private UUID blockingId;

}
