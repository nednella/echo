package com.example.echo_api.exception.custom.internalserver;

import com.cloudinary.Cloudinary;
import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if there is an error during an operation using the {@link Cloudinary}
 * SDK.
 */
public class CloudinaryException extends InternalServerException {

    /**
     * Constructs a {@link CloudinaryException} with the specified {@code details}.
     * 
     * @param details The specific error details.
     */
    public CloudinaryException(String details) {
        super(ErrorMessageConfig.InternalServerError.CLOUDINARY_SDK_ERROR, details);
    }

}
