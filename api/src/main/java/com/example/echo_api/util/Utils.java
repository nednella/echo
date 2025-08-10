package com.example.echo_api.util;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.http.HttpHeaders;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class Utils {

    /**
     * Convert Spring HttpHeaders into Java HttpHeaders.
     * 
     * @param headers {@link org.springframework.http.HttpHeaders}
     * @return {@link java.net.http.HttpHeaders}
     */
    public static java.net.http.HttpHeaders convertHeaders(HttpHeaders headers) {
        return java.net.http.HttpHeaders.of(headers, (t, v) -> true);
    }

}
