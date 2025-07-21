package com.example.echo_api.unit.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

import com.example.echo_api.persistence.mapper.ImageMapper;
import com.example.echo_api.persistence.model.image.Image;
import com.example.echo_api.persistence.model.image.ImageType;
import com.example.echo_api.util.cloudinary.CloudinaryUploadSuccess;

class ImageMapperTest {

    @Test
    void ImageMapper_toEntity() {
        CloudinaryUploadSuccess response = new CloudinaryUploadSuccess(
            null, null, 0, 0, 0, null, null, 0, null, null, null);
        ImageType type = ImageType.AVATAR;
        String transformedUrl = "transformed";
        Image image = ImageMapper.toEntity(response, type, transformedUrl);

        assertNotNull(image);
        assertEquals(type, image.getType());
        assertEquals(response.publicId(), image.getPublicId());
        assertEquals(response.assetId(), image.getAssetId());
        assertEquals(response.width(), image.getOriginalWidth());
        assertEquals(response.height(), image.getOriginalHeight());
        assertEquals(response.url(), image.getOriginalUrl());
        assertEquals(transformedUrl, image.getTransformedUrl());
    }

}
