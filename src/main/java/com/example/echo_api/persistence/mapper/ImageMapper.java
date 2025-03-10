package com.example.echo_api.persistence.mapper;

import static lombok.AccessLevel.PRIVATE;

import com.example.echo_api.persistence.model.image.Image;
import com.example.echo_api.persistence.model.image.ImageType;
import com.example.echo_api.util.cloudinary.CloudinaryUploadSuccess;

import lombok.NoArgsConstructor;

/**
 * Utility mapper to convert information returned via the Cloudinary SDK in the
 * form of a {@link CloudinaryUploadSuccess} object into an {@link Image}
 * entity.
 */
@NoArgsConstructor(access = PRIVATE)
public class ImageMapper {

    public static Image toEntity(CloudinaryUploadSuccess response, ImageType type, String transformedUrl) {
        return new Image(
            type,
            response.publicId(),
            response.assetId(),
            response.width(),
            response.height(),
            response.secureUrl(),
            transformedUrl);
    }

}
