package com.example.echo_api.config;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * Config class for defining client validation messages across the application.
 */
// @formatter:off
@NoArgsConstructor(access = PRIVATE)
public final class ValidationMessageConfig {

    /* File */
    public static final String INVALID_FILE = "File cannot be null or empty.";
    public static final String IMAGE_SIZE_TOO_LARGE = "File size exceeds limit of " + FileConfig.Image.MAX_SIZE_MB + " MB.";
    public static final String IMAGE_FORMAT_UNSUPPORTED = "File type does not match allowed: " + FileConfig.Image.ALLOWED_TYPES;

    /* Pagination */
    public static final String INVALID_OFFSET = "Offset index must not be negative.";
    public static final String INVALID_LIMIT = "Limit must be in the range 1 to 50.";

    /* Account */
    public static final String INVALID_USERNAME = "Username must be 3-15 characters long and can only include alphanumerics or underscores.";
    public static final String INVALID_PASSWORD = "Password must be at least 6 characters long and contain at least 1 letter and 1 number.";
    public static final String CONFIRMATION_PASSWORD_MISMATCH = "Confirmation password does not match the new password.";
    public static final String NEW_PASSWORD_NOT_UNIQUE = "New password cannot be the same as the current password.";

    /* PROFILE */
    public static final String NAME_TOO_LONG = "Name must not exceed 50 characters.";
    public static final String BIO_TOO_LONG = "Bio must not exceed 160 characters.";
    public static final String LOCATION_TOO_LONG = "Location must not exceed 30 characters.";

}
// @formatter:on
