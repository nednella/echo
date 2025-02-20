package com.example.echo_api.service.cloudinary;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.example.echo_api.exception.custom.cloudinary.CloudinaryDeleteOperationException;
import com.example.echo_api.exception.custom.cloudinary.CloudinaryUploadOperationException;
import com.example.echo_api.util.cloudinary.CloudinaryUploadSuccess;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for interacting with the {@link Cloudinary} SDK.
 */
@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;
    private final ObjectMapper objectMapper;

    @Override
    @SuppressWarnings("rawtypes")
    public CloudinaryUploadSuccess uploadFile(MultipartFile file, Map options)
        throws CloudinaryUploadOperationException {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), options);
            return objectMapper.convertValue(result, CloudinaryUploadSuccess.class);
        } catch (Exception ex) {
            throw new CloudinaryUploadOperationException("Upload operation failed: " + ex.getMessage());
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void deleteFile(String assetId, Map options) throws CloudinaryDeleteOperationException {
        // TODO: invalidate cached assets

        try {
            cloudinary.uploader().destroy(assetId, options);
        } catch (Exception ex) {
            throw new CloudinaryDeleteOperationException("Delete operation failed: " + ex.getMessage());
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public String transformImageUrl(String assetId, Transformation transformations) {
        return cloudinary.url().transformation(transformations).generate(assetId);
    }

}
