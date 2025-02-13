package com.example.echo_api.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 
 */
public class OffsetLimitRequest implements Pageable {

    private final int offset;
    private final int limit;
    private final Sort sort;

    /**
     * Creates a new {@link OffsetLimitRequest} with sort parameters applied.
     * 
     * @param offset Zero-indexed starting position, must not be negative.
     * @param limit  Maximum number of items to be returned, must be greater than 0.
     * @param sort   Must not be {@literal null}, use {@link Sort#unsorted()}
     *               instead.
     */
    public OffsetLimitRequest(int offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be negative.");
        }
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be greater than 0.");
        }
        Assert.notNull(sort, "Sort must not be null");

        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    /**
     * Creates a new unsorted {@link OffsetLimitRequest}.
     * 
     * @param offset Zero-indexed starting position, must not be negative.
     * @param limit  Maximum number of items to be returned, must be greater than 0.
     */
    public OffsetLimitRequest(int offset, int limit) {
        this(offset, limit, Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public @NonNull Sort getSort() {
        return sort;
    }

    @Override
    public @NonNull Pageable next() {
        return new OffsetLimitRequest((int) getOffset() + getPageSize(), getPageSize(), getSort());
    }

    @Override
    public @NonNull Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    public Pageable previous() {
        return hasPrevious()
            ? new OffsetLimitRequest((int) getOffset() - getPageSize(), getPageSize(), getSort())
            : this;
    }

    @Override
    public @NonNull Pageable first() {
        return new OffsetLimitRequest(0, getPageSize(), getSort());
    }

    @Override
    public @NonNull Pageable withPage(int pageNumber) {
        throw new UnsupportedOperationException("Method is not supported by offset/limit pagination.");
    }

    @Override
    public boolean hasPrevious() {
        return getOffset() >= getPageSize();
    }

}
