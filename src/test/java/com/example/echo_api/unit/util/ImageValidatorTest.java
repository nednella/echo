package com.example.echo_api.unit.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.exception.custom.file.FileInvalidException;
import com.example.echo_api.exception.custom.file.ImageFormatException;
import com.example.echo_api.exception.custom.file.ImageSizeException;
import com.example.echo_api.util.validator.ImageValidator;

/**
 * Unit test class for {@link ImageValidator}.
 */
class ImageValidatorTest {

    private static MultipartFile validImage;
    private static MultipartFile invalidImageFileSize;
    private static MultipartFile invalidImageFormat;

    @BeforeAll
    static void setup() throws Exception {
        validImage = readFileFromResources("data/valid_image.jpg", "image/jpeg");
        invalidImageFileSize = readFileFromResources("data/invalid_image_file_size.jpg", "image/jpeg");
        invalidImageFormat = readFileFromResources("data/invalid_image_format.pdf", "application/pdf");
    }

    @Test
    void ImageValidator_Validate_Success() {
        assertDoesNotThrow(() -> ImageValidator.validate(validImage));
    }

    @Test
    void ImageValidator_Validate_ThrowFileInvalid() {
        assertThrows(FileInvalidException.class, () -> ImageValidator.validate(null));
    }

    @Test
    void ImageValidator_Validate_ThrowImageSize() {
        assertThrows(ImageSizeException.class, () -> ImageValidator.validate(invalidImageFileSize));
    }

    @Test
    void ImageValidator_Validate_ThrowImageFormat() {
        assertThrows(ImageFormatException.class, () -> ImageValidator.validate(invalidImageFormat));
    }

    private static MultipartFile readFileFromResources(String fileName, String fileType) throws IOException {
        InputStream inputStream = ImageValidatorTest.class.getClassLoader().getResourceAsStream(fileName);

        return new MockMultipartFile(
            "image",
            fileName,
            fileType,
            inputStream);
    }

}
