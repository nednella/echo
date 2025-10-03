package com.example.echo_api.shared.pagination;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class PaginationConstraints {

    public static final int DEFAULT_OFFSET = 0;
    public static final int MIN_OFFSET = 0;

    public static final int DEFAULT_LIMIT = 20;
    public static final int MIN_LIMIT = 1;
    public static final int MAX_LIMIT = 50;

}
