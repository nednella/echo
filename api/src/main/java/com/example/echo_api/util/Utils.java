package com.example.echo_api.util;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.http.HttpHeaders;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class Utils {

    /**
     * Throw an {@link IllegalArgumentException} if the provided object is
     * {@code null}.
     * 
     * @param <T>  The type of {@code obj}
     * @param obj  The object to check for {@code null}
     * @param name The name of the object field to be consumed in the exception
     *             message
     * @return The non-null {@code obj}
     */
    public static <T> T checkNotNull(T obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
        return obj;
    }

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
