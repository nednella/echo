package com.example.echo_api.util.cloudinary;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the successful response from a Cloudinary image upload operation.
 * 
 * <p>
 * For more information, refer to:
 * {@link https://cloudinary.com/documentation/upload_images#upload_response}
 */
// @formatter:off
public record CloudinaryUploadSuccess(
    @JsonProperty("asset_id") String assetId,
    @JsonProperty("public_id") String publicId,
    int version,
    int width,
    int height,
    String format,
    @JsonProperty("resource_type") String resourceType,
    int bytes,
    String type,
    String url,
    @JsonProperty("secure_url") String secureUrl
) {}
// @formatter:on
