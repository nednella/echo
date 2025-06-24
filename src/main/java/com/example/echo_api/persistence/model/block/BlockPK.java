package com.example.echo_api.persistence.model.block;

import java.util.Objects;
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

    private UUID blockedId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof BlockPK))
            return false;

        BlockPK that = (BlockPK) o;

        return this.blockerId.equals(that.blockerId) &&
            this.blockedId.equals(that.blockedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockerId, blockedId);
    }

}
