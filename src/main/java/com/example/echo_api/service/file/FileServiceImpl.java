package com.example.echo_api.service.file;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.exception.custom.internalserver.CloudinaryException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.mapper.ImageMapper;
import com.example.echo_api.persistence.model.image.Image;
import com.example.echo_api.persistence.model.image.ImageType;
import com.example.echo_api.persistence.repository.ImageRepository;
import com.example.echo_api.service.cloudinary.CloudinaryService;
import com.example.echo_api.util.cloudinary.CloudinaryUploadSuccess;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing CRD operations of files, such as
 * {@link Image}.
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final CloudinaryService cloudinaryService;

    private final ImageRepository imageRepository;

    @Override
    public Image createImage(MultipartFile file, ImageType type) throws CloudinaryException {
        Map<String, Object> options = new HashMap<>();
        options.put("resource_type", "image");
        options.put("asset_folder", type.getFolder());

        CloudinaryUploadSuccess response = cloudinaryService.uploadFile(file, options);
        String transformedUrl = cloudinaryService.transformImageUrl(response.publicId(), type.getTransformations());

        Image image = ImageMapper.toEntity(response, type, transformedUrl);
        return imageRepository.save(image);
    }

    @Override
    public void deleteImage(UUID imageId) throws ResourceNotFoundException, CloudinaryException {
        Image image = findById(imageId);

        Map<String, Object> options = new HashMap<>();
        options.put("invalidate", "true"); // invalidates Cloudinary CDN cached asset copies.

        cloudinaryService.deleteFile(image.getPublicId(), options);
        imageRepository.delete(image);
    }

    /**
     * Internal method for obtaining a {@link Image} via {@code id} from the
     * {@link ImageRepository}.
     * 
     * @param id The id to search within the repository.
     * @return The found {@link Image}.
     * @throws ResourceNotFoundException If no image by that id exists.
     */
    private Image findById(UUID id) throws ResourceNotFoundException {
        return imageRepository.findById(id)
            .orElseThrow(ResourceNotFoundException::new);
    }

}
