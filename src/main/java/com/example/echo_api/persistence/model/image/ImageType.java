package com.example.echo_api.persistence.model.image;

import com.cloudinary.Transformation;

/**
 * Enum to enforce consistency across image types, with specific Cloudinary
 * image transformation properties for a given type.
 */
@SuppressWarnings("rawtypes")
public enum ImageType {

    // @formatter:off
    AVATAR("avatars", new Transformation()
    .width(400)
    .height(400)
    .crop("fill")
    .quality("auto")
    .fetchFormat("auto")
    ),
    BANNER("banners", new Transformation()
        .width(1500)
        .height(500)
        .crop("fill")
        .quality("auto")
        .fetchFormat("auto")
    );
    // @formatter:on

    private final String folder;
    private final Transformation transformations;

    ImageType(String folder, Transformation transformations) {
        this.folder = folder;
        this.transformations = transformations;
    }

    public String getFolder() {
        return this.folder;
    }

    public Transformation getTransformations() {
        return this.transformations;
    }

}
