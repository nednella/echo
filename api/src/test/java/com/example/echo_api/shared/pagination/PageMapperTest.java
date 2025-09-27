package com.example.echo_api.shared.pagination;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

/**
 * Unit test class for {@link PageMapper}.
 */
class PageMapperTest {

    private static final String BASE_URI = "/api/v1/posts";

    /**
     * Construct a synthetic list of numbers to simulate paginated content.
     * 
     * <p>
     * Example: {@code constructListFrom(5, 3, 10)} returns {@code ["5","6","7"]}.
     * 
     * @param offset     the starting index for the items
     * @param limit      the maximum number of items to return
     * @param totalItems the total number of items available in the dataset
     * @return a list of items representing a "page" of data
     */
    private List<String> constructListFrom(int offset, int limit, int totalItems) {
        int startValue = offset;
        int size = Math.min(offset + limit, totalItems);

        List<String> list = new ArrayList<>();
        for (int i = startValue; i < size; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    /**
     * Create a {@link Page} with the given parameters.
     * 
     * @param content the content of the page
     * @param offset  the offset used in the pageable
     * @param limit   the maximum number of items returned in the page
     * @param total   the total number of items across all pages
     * @return the {@link PageImpl} instance
     */
    private <T> Page<T> pageOf(List<T> content, int offset, int limit, long total) {
        Pageable pageable = OffsetLimitRequest.of(offset, limit);
        return new PageImpl<>(content, pageable, total);
    }

    @Nested
    class Constructor {

        @Test
        void toDto_ConstructsPageDtoWithExpectedParameters() {
            // arrange
            int offset = 0;
            int limit = 20;
            int totalItemsAvailable = 100;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            Paged<String> dto = PageMapper.toDTO(page, BASE_URI);

            // assert
            assertThat(dto.offset()).isEqualTo(offset);
            assertThat(dto.limit()).isEqualTo(limit);
            assertThat(dto.total()).isEqualTo(totalItemsAvailable);
            assertThat(dto.items()).containsExactlyElementsOf(items);
        }

    }

    @Nested
    class PreviousURI {

        @Test
        void previousExists_WhenCurrentPageIsNotFirstPage() {
            // arrange
            int offset = 20;
            int limit = 20;
            int totalItemsAvailable = 21;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            Paged<String> dto = PageMapper.toDTO(page, BASE_URI);

            // assert
            assertThat(page.hasPrevious()).isTrue(); // assert previous items available
            assertThat(dto.previous()).isNotNull();
            assertThat(dto.previous()).hasToString(BASE_URI + "?offset=0&limit=20");
        }

        @Test
        void previousExists_WithMisalignedOffset_WhenCurrentPageIsNotFirstPage() {
            // arrange
            int offset = 26; // offset not multiple of limit
            int limit = 20;
            int totalItemsAvailable = 200;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            Paged<String> dto = PageMapper.toDTO(page, BASE_URI);

            // assert
            assertThat(page.hasPrevious()).isTrue(); // assert previous items available
            assertThat(dto.previous()).isNotNull();
            assertThat(dto.previous()).hasToString(BASE_URI + "?offset=6&limit=20");
        }

        @Test
        void previousIsNull_OnFirstPage() {
            // arrange
            int offset = 0;
            int limit = 20; // (0...19)
            int totalItemsAvailable = 100;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            Paged<String> dto = PageMapper.toDTO(page, BASE_URI);

            // assert
            assertThat(page.isFirst()).isTrue();
            assertThat(dto.previous()).isNull();
        }

        @Test
        void previousIsNull_OnFirstPage_WithMisalignedOffset() {
            // arrange
            int offset = 13;
            int limit = 20; // (13..14)
            int totalItemsAvailable = 15;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            Paged<String> dto = PageMapper.toDTO(page, BASE_URI);

            // assert
            assertThat(page.isFirst()).isTrue();
            assertThat(dto.previous()).isNull();
        }

        @Test
        void previous_CorrectlyReducesOffsetEqualToLimit_WhenPreviousPageAvailable() {
            // arrange
            int offset = 20;
            int limit = 20;
            int totalItemsAvailable = 40;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);
            String expectedPagination = String.format("?offset=%d&limit=%d", offset - limit, offset);

            // act
            Paged<String> dto = PageMapper.toDTO(page, BASE_URI);

            // assert
            assertThat(page.hasPrevious()).isTrue(); // assert previous items available
            assertThat(dto.previous()).asString().contains(expectedPagination);
        }

        @Test
        void previous_PreservesExistingUnrelatedQueryParams() {
            // arrange
            int offset = 20;
            int limit = 20;
            int totalItemsAvailable = 40;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            String baseUriWithSomeExistingUnrelatedQueries = BASE_URI + "?filter=active&sort=date";
            Paged<String> dto = PageMapper.toDTO(page, baseUriWithSomeExistingUnrelatedQueries);

            // assert
            assertThat(dto.previous()).asString().contains("?filter=active&sort=date");
        }

        @Test
        void previous_ReplacesExistingPaginationQueryParams() {
            // arrange
            int offset = 20;
            int limit = 20;
            int totalItemsAvailable = 40;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            String baseUriWithSomeExistingPaginationQueries = BASE_URI + "?offset=200&limit=5";
            Paged<String> dto = PageMapper.toDTO(page, baseUriWithSomeExistingPaginationQueries);

            // assert
            assertThat(dto.previous()).asString().doesNotContain("?offset=200&limit=5");
        }

    }

    @Nested
    class NextURI {

        @Test
        void nextExists_OnFirstPage_WhenThereIsMoreDataAvailable() {
            // arrange
            int offset = 0;
            int limit = 20;
            int totalItemsAvailable = 21;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            Paged<String> dto = PageMapper.toDTO(page, BASE_URI);

            // assert
            assertThat(page.hasNext()).isTrue(); // assert more items available
            assertThat(dto.next()).isNotNull();
            assertThat(dto.next()).hasToString(BASE_URI + "?offset=20&limit=20");
        }

        @Test
        void nextExists_WithMisalignedOffset_WhenThereIsMoreDataAvailable() {
            // arrange
            int offset = 6; // offset not multiple of limit
            int limit = 20;
            int totalItemsAvailable = 200;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            Paged<String> dto = PageMapper.toDTO(page, BASE_URI);

            // assert
            assertThat(page.hasNext()).isTrue(); // assert more items available
            assertThat(dto.next()).isNotNull();
            assertThat(dto.next()).hasToString(BASE_URI + "?offset=26&limit=20");
        }

        @Test
        void nextIsNull_OnLastPage() {
            // arrange
            int offset = 80;
            int limit = 20; // (80...99)
            int totalItemsAvailable = 100;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            Paged<String> dto = PageMapper.toDTO(page, BASE_URI);

            // assert
            assertThat(page.isLast()).isTrue(); // assert no more items available
            assertThat(dto.next()).isNull();
        }

        @Test
        void nextIsNull_OnLastPage_WithMisalignedOffset() {
            // arrange
            int offset = 13;
            int limit = 20; // (13..14)
            int totalItemsAvailable = 15;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            Paged<String> dto = PageMapper.toDTO(page, BASE_URI);

            // assert
            assertThat(page.isLast()).isTrue(); // assert no more items available
            assertThat(dto.next()).isNull();
        }

        @Test
        void next_CorrectlyIncreasesOffsetEqualToLimit_WhenNextPageAvailable() {
            // arrange
            int offset = 20;
            int limit = 20;
            int totalItemsAvailable = 41;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);
            String expectedPagination = String.format("?offset=%d&limit=%d", offset + limit, offset);

            // act
            Paged<String> dto = PageMapper.toDTO(page, BASE_URI);

            // assert
            assertThat(page.hasNext()).isTrue(); // assert more items available
            assertThat(dto.next()).asString().contains(expectedPagination);
        }

        @Test
        void next_PreservesExistingUnrelatedQueryParams() {
            // arrange
            int offset = 0;
            int limit = 20;
            int totalItemsAvailable = 100;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            String baseUriWithSomeExistingUnrelatedQueries = BASE_URI + "?filter=active&sort=date";
            Paged<String> dto = PageMapper.toDTO(page, baseUriWithSomeExistingUnrelatedQueries);

            // assert
            assertThat(dto.next()).asString().contains("?filter=active&sort=date");
        }

        @Test
        void next_ReplacesExistingPaginationQueryParams() {
            // arrange
            int offset = 0;
            int limit = 20;
            int totalItemsAvailable = 100;
            List<String> items = constructListFrom(offset, limit, totalItemsAvailable);
            Page<String> page = pageOf(items, offset, limit, totalItemsAvailable);

            // act
            String baseUriWithSomeExistingPaginationQueries = BASE_URI + "?offset=200&limit=5";
            Paged<String> dto = PageMapper.toDTO(page, baseUriWithSomeExistingPaginationQueries);

            // assert
            assertThat(dto.next()).asString().doesNotContain("?offset=200&limit=5");
        }

    }

}
