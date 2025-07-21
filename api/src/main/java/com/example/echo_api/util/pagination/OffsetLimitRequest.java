package com.example.echo_api.util.pagination;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

/**
 * A custom implementation of {@link Pageable} that supports pagination using
 * <b>offset</b> and <b>limit</b> instead of the traditional page number and
 * page size.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * OffsetLimitRequest pageRequest = new OffsetLimitRequest(10, 20); // Fetch 20 items starting from the 10th item
 * Page<Item> page = repository.findAll(pageRequest);
 * </pre>
 * 
 * @see Pageable
 * @see Sort
 */
public class OffsetLimitRequest implements Pageable {

    private final int offset;
    private final int limit;
    private final Sort sort;

    /**
     * Creates a new {@link OffsetLimitRequest} with sort parameters applied.
     * 
     * @param offset Zero-indexed starting position.
     * @param limit  Maximum number of items to be returned.
     * @param sort   Sort parameters for the data.
     * @throws IllegalArgumentException if {@code offset} is negative..
     * @throws IllegalArgumentException if {@code limit} is less than 1.
     * @throws IllegalArgumentException if {@code sort} is {@literal null}.
     */
    public OffsetLimitRequest(int offset, int limit, Sort sort) throws IllegalArgumentException {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be negative.");
        }
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be greater than 0.");
        }
        if (sort == null) {
            throw new IllegalArgumentException("Sort must not be null");
        }

        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    /**
     * Creates a new unsorted {@link OffsetLimitRequest}.
     * 
     * @param offset Zero-indexed starting position.
     * @param limit  Maximum number of items to be returned.
     */
    public OffsetLimitRequest(int offset, int limit) throws IllegalArgumentException {
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
