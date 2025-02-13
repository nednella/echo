package com.example.echo_api.persistence.mapper;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.data.domain.Page;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.echo_api.persistence.dto.response.pagination.PageDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;

/**
 * Utility mapper to convert the default Spring pagination {@link Page} to a
 * custom {@link PageDTO}, which returns more concise information to the client
 * than the default.
 */
@NoArgsConstructor(access = PRIVATE)
public class PageMapper {

    public static <T> PageDTO<T> toDTO(Page<T> page, int offset, int limit) {
        String path = getCurrentUri();
        String previous = page.hasPrevious() ? constructPreviousUri(path, offset, limit) : null;
        String next = page.hasNext() ? constructNextUri(path, offset, limit) : null;

        return new PageDTO<>(
            previous,
            next,
            offset,
            limit,
            (int) page.getTotalElements(),
            page.getContent());
    }

    private static String getCurrentUri() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
            .currentRequestAttributes()).getRequest();

        return request.getRequestURI();
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
