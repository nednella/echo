package com.example.echo_api.unit.util;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import org.springframework.data.domain.Pageable;

import com.example.echo_api.util.pagination.OffsetLimitRequest;

/**
 * Unit test class for {@link OffsetLimitRequest}.
 */
class OffsetLimitRequestTest {

    @Test
    void contructor_ShouldCreateWhenValidOffsetAndLimitSupplied() {
        int offset = 0;
        int limit = 10;
        var page = new OffsetLimitRequest(offset, limit);

        assertThat(page.getOffset()).isEqualTo(offset);
        assertThat(page.getPageSize()).isEqualTo(limit);
    }

    @Test
    void contructor_ShouldThrowWhenOffsetNegative() {
        int offset = -1;
        int limit = 10;

        assertThrows(IllegalArgumentException.class, () -> new OffsetLimitRequest(offset, limit));
    }

    @Test
    void contructor_ShouldThrowWhenLimitLessThanOne() {
        int offset = 0;
        int limit = 0;

        assertThrows(IllegalArgumentException.class, () -> new OffsetLimitRequest(offset, limit));
    }

    @Test
    void next_ShouldIncreaseOffsetByLimit() {
        int offset = 0;
        int limit = 10;
        var page = new OffsetLimitRequest(offset, limit);

        var next = page.next();
        assertThat(next.getOffset()).isEqualTo(offset + limit);
    }

    @Test
    void previousOrFirst_ShouldReturnPreviousPageWhenNotFirstPage() {
        int offset = 20;
        int limit = 10;
        var page = new OffsetLimitRequest(offset, limit);

        // assert not first page
        int pageNumber = page.getPageNumber();
        assertThat(pageNumber).isNotZero(); // pages are 0-indexed

        // assert returns previous page
        var prevOrFirst = page.previousOrFirst();
        assertThat(prevOrFirst.getPageNumber()).isEqualTo(pageNumber - 1);
    }

    @Test
    void previousOrFirst_ShouldReturnFirstPageWhenNoPreviousPage() {
        int offset = 0;
        int limit = 10;
        var page = new OffsetLimitRequest(offset, limit);

        // assert first page
        int pageNumber = page.getPageNumber();
        assertThat(pageNumber).isZero(); // pages are 0-indexed

        // assert returns same page
        var prevOrFirst = page.previousOrFirst();
        assertThat(prevOrFirst.getPageNumber()).isEqualTo(pageNumber);
    }

    @Test
    void previous_ShouldDecreaseOffsetByLimitWhenHasPreviousPages() {
        int offset = 30;
        int limit = 10;
        var page = new OffsetLimitRequest(offset, limit);

        // assert not first page
        int pageNumber = page.getPageNumber();
        assertThat(pageNumber).isNotZero(); // pages are 0-indexed

        // assert returns previous page
        Pageable prev = page.previous();
        assertThat(prev.getPageNumber()).isEqualTo(pageNumber - 1);
    }

    @Test
    void previous_ShouldReturnSamePageWhenHasNoPreviousPages() {
        int offset = 0;
        int limit = 10;
        var page = new OffsetLimitRequest(offset, limit);

        // assert first page
        int pageNumber = page.getPageNumber();
        assertThat(pageNumber).isZero(); // pages are 0-indexed

        // assert returns same page
        var previous = page.previous();
        assertThat(previous.getPageNumber()).isEqualTo(pageNumber);
    }

    @Test
    void first_ShouldReturnPageWithOffsetZero() {
        int offset = 100;
        int limit = 10;
        var page = new OffsetLimitRequest(offset, limit);

        // assert offset = 0
        var first = page.first();
        assertThat(first.getOffset()).isZero();
    }

    @Test
    void hasPrevious_ShouldReturnTrueWhenOffsetGreaterThanLimit() {
        int offset = 100;
        int limit = 10;
        var page = new OffsetLimitRequest(offset, limit);

        assertThat(page.hasPrevious()).isTrue();
    }

    @Test
    void hasPrevious_ShouldReturnTrueWhenOffsetLessThanLimit() {
        int offset = 6;
        int limit = 10;
        var page = new OffsetLimitRequest(offset, limit);

        assertThat(page.hasPrevious()).isFalse();
    }

}
