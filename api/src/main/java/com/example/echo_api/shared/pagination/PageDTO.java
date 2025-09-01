package com.example.echo_api.shared.pagination;

import java.net.URI;
import java.util.List;

/**
 * Represents a standardised pagination response format for the application,
 * intended to replace the Spring default
 * {@link org.springframework.data.domain.Page}.
 * 
 * <p>
 * Accepts a generic type {@code T} for the content type.
 * 
 * @param previous the {@link URI} to fetch the previous page, assuming the same
 *                 page size
 * @param next     the {@link URI} to fetch the next page, assuming the same
 *                 page size
 * @param limit    the size of this page
 * @param offset   the offset of data fetched in this page (page size * page
 *                 number)
 * @param total    the total number of items available from this resource
 * @param items    the list of content returned in this response
 */
public record PageDTO<T>(
    URI previous,
    URI next,
    int offset,
    int limit,
    int total,
    List<T> items
) {}
