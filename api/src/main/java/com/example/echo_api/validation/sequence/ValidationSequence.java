package com.example.echo_api.validation.sequence;

import jakarta.validation.GroupSequence;

/**
 * A custom Validation sequence to ensure basic checks, such as
 * {@code @NotNull}, {@code @NotBlank}, are validated before advanced checks
 * such as {@code @Username}, {@code @Password}.
 */
@GroupSequence({ Basic.class, Advanced.class })
public interface ValidationSequence {
}
