package com.example.echo_api.config;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * Config class for defining client validation messages across the application.
 */
// @formatter:off
@NoArgsConstructor(access = PRIVATE)
public final class ValidationMessageConfig {

    /* Pagination */
    public static final String INVALID_OFFSET = "Offset index must not be negative";
    public static final String INVALID_LIMIT = "Limit must be in the range 1 to 50";

    /* PROFILE */
    public static final String NAME_TOO_LONG = "Name must not exceed 50 characters";
    public static final String BIO_TOO_LONG = "Bio must not exceed 160 characters";
    public static final String LOCATION_TOO_LONG = "Location must not exceed 30 characters";

    /* POST */
    public static final String TEXT_TOO_LONG = "Text must not exceed 280 characters";
    public static final String TEXT_NULL_OR_BLANK = "Text cannot be null or blank";
    public static final String INVALID_PARENT_ID = "Invalid parent post ID";

}
// @formatter:on
