package com.example.echo_api.service.cloudinary;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.example.echo_api.exception.custom.cloudinary.CloudinaryDeleteOperationException;
import com.example.echo_api.exception.custom.cloudinary.CloudinaryUploadOperationException;
import com.example.echo_api.util.cloudinary.CloudinaryUploadSuccess;

public interface CloudinaryService {

    /**
     * Uploads a file to Cloudinary using the {@link Cloudinary} SDK with the
     * specified {@code options}.
     * 
     * @param file    The file to upload. Must not be null.
     * @param options A map of options to use during the destroy operation. Can be
     *                null.
     * @return A {@link CloudinaryUploadSuccess} object containing information about
     *         the uploaded file.
     * @throws CloudinaryUploadOperationException If the upload operation fails due
     *                                            to a Cloudinary-related error
     *                                            (e.g., invalid credentials,
     *                                            network error, rate limited).
     */
    @SuppressWarnings("rawtypes")
    public CloudinaryUploadSuccess uploadFile(MultipartFile file, Map options)
        throws CloudinaryUploadOperationException;

    /**
     * Destroys a file from Cloudinary using the {@link Cloudinary} SDK with the
     * specified {@code options}.
     * 
     * @param publicId The public ID of the file to destroy. Must not be null.
     * @param options  A map of options to use during the destroy operation. Can be
     *                 null.
     * @throws CloudinaryDeleteOperationException If the delete operation fails due
     *                                            to a Cloudinary-related error
     *                                            (e.g., resource not found, invalid
     *                                            credentials, network error).
     */
    @SuppressWarnings("rawtypes")
    public void deleteFile(String publicId, Map options) throws CloudinaryDeleteOperationException;

    /**
     * Generates a transformed image URL for the specified Cloudinary asset using
     * the provided {@link Transformation} object.
     * 
     * @param publicId        The Cloudinary public ID of the asset to transform.
     * @param transformations The {@link Transformation} object containing the
     *                        modifications to apply to the asset.
     * @return A String representing the transformed URL.
     */
    @SuppressWarnings("rawtypes")
    public String transformImageUrl(String publicId, Transformation transformations);

}
