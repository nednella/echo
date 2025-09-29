package com.example.echo_api.exception;

import org.springframework.http.HttpStatus;

/**
 * Defines a contract for {@link ErrorCode}s that are used to build uniform API
 * error responses.
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
     * Returns the HTTP status associated with this {@link ErrorCode}.
     * 
     * @return {@link HttpStatus} that should be returned to the client
     */
    HttpStatus getStatus();

    /**
     * Returns the error message template for this {@link ErrorCode}.
     * 
     * <p>
     * This template is typically a {@link String#format}-compatible pattern, for
     * example {@code "User with id %s not found"}.
     * 
     * @return the message template string
     */
    String getMessageTemplate();

    /**
     * Returns the number of arguments expected by this {@link ErrorCode} message
     * template.
     * 
     * @return the number of arguments the template expects
     */
    int getExpectedArgs();

    /**
     * Formats this {@link ErrorCode} message template with the provided arguments.
     * 
     * @param args the arguments to insert into the message template
     * @return the formatted error message to be returned to the client
     * @throws IllegalArgumentException if the number of provided arguments does not
     *                                  match the expected for this
     *                                  {@link ErrorCode}
     */
    default String formatMessage(Object... args) {
        validateArgumentCount(args.length);
        return String.format(getMessageTemplate(), args);
    }

    /**
     * Return an {@link ApplicationException} representing this {@link ErrorCode}
     * with the provided arguments.
     * 
     * @param args the arguments to insert into the message template
     * @return the {@link ApplicationException} to be thrown
     * @throws IllegalArgumentException if the number of provided arguments does not
     *                                  match the expected for this
     *                                  {@link ErrorCode}
     */
    default ApplicationException buildAsException(Object... args) {
        validateArgumentCount(args.length);
        return new ApplicationException(this, args);
    }

    /**
     * validate the number of arguments supplied against the expected count for this
     * {@link ErrorCode}.
     * 
     * @param actual the actual number of arguments provided
     * @throws IllegalArgumentException if the number of provided arguments does not
     *                                  match the expected for this
     *                                  {@link ErrorCode}
     */
    private void validateArgumentCount(int actual) {
        int expected = getExpectedArgs();
        if (actual != expected) {
            String name = getClass().getSimpleName();

            throw new IllegalArgumentException(
                String.format("Expected %d arguments for %s, but got %d", expected, name, actual));
        }
    }

}
