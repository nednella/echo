package com.example.echo_api.shared.pagination;

import org.springdoc.core.annotations.ParameterObject;

import com.example.echo_api.shared.constant.PaginationConstraints;
import com.example.echo_api.shared.validation.annotations.Limit;
import com.example.echo_api.shared.validation.annotations.Offset;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ParameterObject
public class PageParameters {

    @Offset
    @Parameter(description = "Zero-based offset", schema = @Schema(defaultValue = "0", minimum = "0"))
    int offset = PaginationConstraints.DEFAULT_OFFSET;

    @Limit
    @Parameter(description = "Page size", schema = @Schema(defaultValue = "20", minimum = "1", maximum = "50"))
    int limit = PaginationConstraints.DEFAULT_LIMIT;

}
