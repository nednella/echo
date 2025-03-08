package com.example.echo_api.util.validator;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.exception.custom.file.ImageFormatException;
import com.example.echo_api.exception.custom.file.ImageSizeException;

import lombok.NoArgsConstructor;

import com.example.echo_api.exception.custom.file.FileInvalidException;

/**
 * Validator class for {@link MultipartFile} pertaining to images, ensuring
 * uploaded images meet size and type requirements.
 */
@Component
@NoArgsConstructor(access = PRIVATE)
public class ImageValidator {

    private static final long MAX_SIZE_MB = 2;
    private static final long MAX_SIZE_BYTES = MAX_SIZE_MB * 1024 * 1024;
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");

    public static boolean supports(@NonNull Class<?> clazz) {
        return MultipartFile.class.equals(clazz);
    }

    public static void validate(@NonNull Object target) {
        MultipartFile file = (MultipartFile) target;

        if (file.isEmpty()) {
            throw new FileInvalidException();
        }

        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new ImageSizeException();
        }

        String fileType = file.getContentType();
        if (fileType == null || !ALLOWED_TYPES.contains(fileType.toLowerCase())) {
            throw new ImageFormatException();
        }
    }

}
