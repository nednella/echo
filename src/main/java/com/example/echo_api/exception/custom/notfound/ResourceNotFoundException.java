package com.example.echo_api.exception.custom.notfound;

import com.example.echo_api.config.ErrorMessageConfig;

/**
 * Thrown if there is an empty {@link Optional} returned from the repository
 * layer when requesting an entity.
 */
public class ResourceNotFoundException extends NotFoundException {

    /**
     * Constructs a {@link ResourceNotFoundException} with a default custom message.
     */
    public ResourceNotFoundException() {
        super(ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND);
    }

}
