package com.example.echo_api.unit.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.exception.custom.badrequest.InvalidFileException;
import com.example.echo_api.exception.custom.badrequest.InvalidImageFormatException;
import com.example.echo_api.exception.custom.badrequest.InvalidImageSizeException;
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
        assertThrows(InvalidFileException.class, () -> ImageValidator.validate(null));
    }

    @Test
    void ImageValidator_Validate_ThrowImageFormat() {
        assertThrows(InvalidImageFormatException.class, () -> ImageValidator.validate(invalidImageFormat));
    }

    @Test
    void ImageValidator_Validate_ThrowImageSize() {
        assertThrows(InvalidImageSizeException.class, () -> ImageValidator.validate(invalidImageFileSize));
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
