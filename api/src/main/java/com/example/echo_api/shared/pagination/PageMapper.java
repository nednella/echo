package com.example.echo_api.shared.pagination;

import static lombok.AccessLevel.PRIVATE;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.NoArgsConstructor;

/**
 * Utility mapper to convert the default Spring pagination {@link Page} to a
 * custom {@link PageDTO}, which returns more concise information to the client
 * than the default.
 */
@NoArgsConstructor(access = PRIVATE)
public class PageMapper {

    public static <T> PageDTO<T> toDTO(Page<T> page, String uri) {
        int offset = (int) page.getPageable().getOffset();
        int limit = page.getPageable().getPageSize();

        URI previous = page.hasPrevious() ? constructUri(uri, offset - limit, limit) : null;
        URI next = page.hasNext() ? constructUri(uri, offset + limit, limit) : null;

        return new PageDTO<>(
            previous,
            next,
            offset,
            limit,
            (int) page.getTotalElements(),
            page.getContent());
    }

    private static URI constructUri(String baseUri, int offset, int limit) {
        return UriComponentsBuilder
            .fromUriString(baseUri)
            .replaceQueryParam("offset", offset)
            .replaceQueryParam("limit", limit)
            .build()
            .toUri();
    }

}
