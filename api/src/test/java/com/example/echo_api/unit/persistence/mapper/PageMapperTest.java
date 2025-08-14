package com.example.echo_api.unit.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.util.pagination.OffsetLimitRequest;

class PageMapperTest {

    @Test
    void PageMapper_toDTO() {
        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 1;
        Pageable pageable = new OffsetLimitRequest(offset, limit);
        List<String> content = List.of("test");
        Page<String> page = new PageImpl<>(content, pageable, 0);
        PageDTO<String> pageDto = PageMapper.toDTO(page, uri);

        assertNull(pageDto.previous());
        assertNull(pageDto.next());
        assertEquals(offset, pageDto.offset());
        assertEquals(limit, pageDto.limit());
        assertEquals(content.size(), pageDto.total());
        assertEquals(content, pageDto.items());
    }

}
