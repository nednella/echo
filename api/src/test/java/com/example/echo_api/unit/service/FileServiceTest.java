package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.exception.custom.internalserver.CloudinaryException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.mapper.ImageMapper;
import com.example.echo_api.persistence.model.image.Image;
import com.example.echo_api.persistence.model.image.ImageType;
import com.example.echo_api.persistence.repository.ImageRepository;
import com.example.echo_api.service.cloudinary.CloudinaryService;
import com.example.echo_api.service.file.FileService;
import com.example.echo_api.service.file.FileServiceImpl;
import com.example.echo_api.util.cloudinary.CloudinaryUploadSuccess;

/**
 * Unit test class for {@link FileService}.
 */
@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    @Test
    void FileService_CreateImage_ReturnImage() throws Exception {
        // arrange
        CloudinaryUploadSuccess response = new CloudinaryUploadSuccess(
            null, null, 0, 0, 0, null, null, 0, null, null, null);
        Image expected = ImageMapper.toEntity(response, ImageType.AVATAR, null);

        Map<String, Object> options = new HashMap<>();
        options.put("resource_type", "image");
        options.put("asset_folder", ImageType.AVATAR.getFolder());

        when(cloudinaryService.uploadFile(null, options)).thenReturn(response);
        when(imageRepository.save(any(Image.class))).thenReturn(expected);

        // act
        Image actual = fileService.createImage(null, ImageType.AVATAR);

        // assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void FileService_CreateImage_ThrowCloudinaryException() {
        // arrange
        Map<String, Object> options = new HashMap<>();
        options.put("resource_type", "image");
        options.put("asset_folder", ImageType.AVATAR.getFolder());

        when(cloudinaryService.uploadFile(null, options)).thenThrow(new CloudinaryException(null));

        // act & assert
        assertThrows(CloudinaryException.class, () -> fileService.createImage(null, ImageType.AVATAR));
    }

    @Test
    void FileService_DeleteImage_ReturnVoid() {
        // assert
        UUID imageId = UUID.randomUUID();
        String assetId = UUID.randomUUID().toString();

        Map<String, Object> options = new HashMap<>();
        options.put("invalidate", "true");

        Image image = new Image(null, null, assetId, 0, 0, null, null);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        doNothing().when(cloudinaryService).deleteFile(null, options);
        doNothing().when(imageRepository).delete(image);

        // act & assert
        assertDoesNotThrow(() -> fileService.deleteImage(imageId));
    }

    @Test
    void FileService_DeleteImage_ThrowResourceNotFound() {
        // assert
        UUID imageId = UUID.randomUUID();
        when(imageRepository.findById(imageId)).thenThrow(new ResourceNotFoundException());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> fileService.deleteImage(imageId));
    }

    @Test
    void FileService_DeleteImage_ThrowCloudinaryException() {
        // assert
        UUID imageId = UUID.randomUUID();
        String assetId = UUID.randomUUID().toString();

        Map<String, Object> options = new HashMap<>();
        options.put("invalidate", "true");

        Image image = new Image(null, null, assetId, 0, 0, null, null);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
        doThrow(new CloudinaryException(null)).when(cloudinaryService).deleteFile(null, options);

        // act & assert
        assertThrows(CloudinaryException.class, () -> fileService.deleteImage(imageId));
    }

}
