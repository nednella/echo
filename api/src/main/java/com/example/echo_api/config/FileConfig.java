package com.example.echo_api.config;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.NoArgsConstructor;

/**
 * Config class for defining {@link MultipartFile}-related settings across the
 * application.
 */
@NoArgsConstructor(access = PRIVATE)
public class FileConfig {

    @NoArgsConstructor(access = PRIVATE)
    static class Image {

        public static final long MAX_SIZE_MB = 2;
        public static final long MAX_SIZE_BYTES = MAX_SIZE_MB * 1024 * 1024;
        public static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");

    }

}
