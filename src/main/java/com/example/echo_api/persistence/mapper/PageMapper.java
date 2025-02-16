package com.example.echo_api.persistence.mapper;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.data.domain.Page;

import com.example.echo_api.persistence.dto.response.pagination.PageDTO;

import lombok.NoArgsConstructor;

/**
 * Utility mapper to convert the default Spring pagination {@link Page} to a
 * custom {@link PageDTO}, which returns more concise information to the client
 * than the default.
 */
@NoArgsConstructor(access = PRIVATE)
public class PageMapper {

    public static <T> PageDTO<T> toDTO(Page<T> page, String uri, int offset, int limit) {
        String previous = page.hasPrevious() ? constructPreviousUri(uri, offset, limit) : null;
        String next = page.hasNext() ? constructNextUri(uri, offset, limit) : null;

        return new PageDTO<>(
            previous,
            next,
            offset,
            limit,
            (int) page.getTotalElements(),
            page.getContent());
    }

    private static String constructPreviousUri(String path, int offset, int limit) {
        String pagination = String.format("?offset=%d&limit=%d", offset - limit, limit);
        return path + pagination;
    }

    private static String constructNextUri(String path, int offset, int limit) {
        String pagination = String.format("?offset=%d&limit=%d", offset + limit, limit);
        return path + pagination;
    }

}
