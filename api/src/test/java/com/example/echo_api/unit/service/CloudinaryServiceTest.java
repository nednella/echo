package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.echo_api.exception.custom.internalserver.CloudinaryException;
import com.example.echo_api.service.cloudinary.CloudinaryService;
import com.example.echo_api.service.cloudinary.CloudinaryServiceImpl;
import com.example.echo_api.util.cloudinary.CloudinaryUploadSuccess;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link CloudinaryService}.
 */
@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    @Test
    void CloudinaryService_UploadFile_ReturnCloudinaryUploadSuccess() throws IOException {
        // arrange
        MultipartFile file = readFileFromResources("data/valid_image.jpg", "image/jpeg");
        Map<String, String> result = new HashMap<>();
        CloudinaryUploadSuccess expected = new CloudinaryUploadSuccess(
            null, null, 0, 0, 0, null, null, 0, null, null, null);

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(file.getBytes(), null)).thenReturn(result);
        when(objectMapper.convertValue(result, CloudinaryUploadSuccess.class)).thenReturn(expected);

        // act
        CloudinaryUploadSuccess actual = cloudinaryService.uploadFile(file, null);

        // assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void CloudinaryService_UploadFile_ThrowCloudinaryException() throws IOException {
        // arrange
        MultipartFile file = readFileFromResources("data/valid_image.jpg", "image/jpeg");
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(file.getBytes(), null)).thenThrow(new IOException());

        // act & assert
        assertThrows(CloudinaryException.class, () -> cloudinaryService.uploadFile(file, null));
    }

    @Test
    void CloudinaryService_DeleteFile_ReturnVoid() throws IOException {
        // arrange
        String assetId = UUID.randomUUID().toString();
        Map<String, String> result = new HashMap<>();
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(assetId, null)).thenReturn(result);

        // act & assert
        assertDoesNotThrow(() -> cloudinaryService.deleteFile(assetId, null));
    }

    @Test
    void CloudinaryService_DeleteFile_ThrowCloudinaryException() throws IOException {
        // arrange
        String assetId = UUID.randomUUID().toString();
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(assetId, null)).thenThrow(new IOException());

        // act & assert
        assertThrows(CloudinaryException.class, () -> cloudinaryService.deleteFile(assetId, null));
    }

    @Test
    void CloudinaryService_TransformImageUrl_ReturnString() {
    }

    private MultipartFile readFileFromResources(String fileName, String fileType) throws IOException {
        InputStream inputStream = CloudinaryServiceTest.class.getClassLoader().getResourceAsStream(fileName);

        return new MockMultipartFile(
            "image",
            fileName,
            fileType,
            inputStream);
    }

}
