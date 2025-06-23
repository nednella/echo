package com.example.echo_api.exception.custom.internalserver;

import com.example.echo_api.config.ErrorMessageConfig;
import com.twitter.twittertext.Extractor;

/**
 * Thrown if there is an error when converting {@link Enum} types during an
 * operation using the TwitterText {@link Extractor}.
 */
public class TwitterTextEnumException extends InternalServerException {

    /**
     * Constructs a {@link TwitterTextEnumException} with a default message and
     * specified {@code details}.
     * 
     * @param details The specific error details.
     */
    public TwitterTextEnumException(String details) {
        super(ErrorMessageConfig.InternalServerError.TWITTER_TEXT_ENUM_CONVERSION, details);
    }

}
