package com.example.echo_api.service.cloudinary;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
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
     * @param assetId The immutable asset ID of the file to destroy. Must not be
     *                null.
     * @param options A map of options to use during the destroy operation. Can be
     *                null.
     * @throws CloudinaryDeleteOperationException If the delete operation fails due
     *                                            to a Cloudinary-related error
     *                                            (e.g., resource not found, invalid
     *                                            credentials, network error).
     */
    @SuppressWarnings("rawtypes")
    public void deleteFile(String assetId, Map options) throws CloudinaryDeleteOperationException;

}
