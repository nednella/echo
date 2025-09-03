package com.example.echo_api.exception;

import org.springframework.http.HttpStatus;

/**
 * Defines a contract for error codes that are used to build uniform API error
 * responses.
 * 
 * <p>
 * Should be implemented by {@code enum} classes, where each implementing class
 * should declare the following fields:
 * 
 * <pre>
 * 
 * private final HttpStatus status;
 * private final String messageTemplate;
 * private final int expectedArgs;
 * </pre>
 */
public interface ErrorCode {

    /**
     * Returns the HTTP status associated with the error.
     * 
     * @return {@link HttpStatus} that should be returned to the client
     */
    HttpStatus getStatus();

    /**
     * Returns the error message template for the error.
     * 
     * <p>
     * This template is typically a {@link String#format}-compatible pattern, for
     * example {@code "User with id %s not found"}.
     * 
     * @return the message template string
     */
    String getMessageTemplate();

    /**
     * Returns the number of arguments expected by the message template.
     * 
     * @return the number of arguments the template expects
     */
    int getExpectedArgs();

    /**
     * Formats the message template with the provided arguments.
     * 
     * <p>
     * This method verifies that the number of supplied arguments matches the
     * {@link #getExpectedArgs() expected} count. If there is a mismatch, an
     * {@link IllegalArgumentException} is thrown.
     * 
     * @param args the arguments to insert into the message template
     * @return the formatted error message
     * @throws IllegalArgumentException if the number of provided arguments does not
     *                                  match {@link #getExpectedArgs()}
     */
    default String formatMessage(Object... args) {
        int actual = args.length;
        int expected = getExpectedArgs();

        if (actual != expected) {
            String name = getClass().getSimpleName();

            throw new IllegalArgumentException(
                String.format("Expected %d arguments for %s, but got %d", expected, name, actual));
        }

        return String.format(getMessageTemplate(), args);
    }

}
