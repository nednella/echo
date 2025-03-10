package com.example.echo_api.service.file;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.exception.custom.cloudinary.CloudinaryException;
import com.example.echo_api.exception.custom.image.ImageException;
import com.example.echo_api.persistence.model.image.Image;
import com.example.echo_api.persistence.model.image.ImageType;

public interface FileService {

    /**
     * Uploads an image to Cloudinary and uses the Cloudinary response to create and
     * store a reference {@link Image} in the database.
     * 
     * @param file The image file to upload.
     * @param type The image type to upload.
     * @return The saved {@link Image} entity referencing the uploaded image.
     * @throws CloudinaryException If the upload operation fails.
     */
    public Image createImage(MultipartFile file, ImageType type) throws CloudinaryException;

    /**
     * Deletes an image from Cloudinary and the repository in that order.
     * 
     * @param imageId The image id to delete.
     * @throws ImageException      If the image id is not found.
     * @throws CloudinaryException if the destroy operation fails.
     */
    public void deleteImage(UUID imageId) throws ImageException, CloudinaryException;

}
