package app.echo_social.modules.post.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import app.echo_social.exception.ErrorResponse;
import app.echo_social.modules.post.dto.response.PostDTO;
import app.echo_social.shared.constant.ApiRoutes;
import app.echo_social.shared.pagination.PageParameters;
import app.echo_social.shared.pagination.Paged;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Post API")
@Validated
public interface PostViewAPI {

    @Operation(description = "Get a post by ID")
    @ApiResponse(responseCode = "404", description = "ID not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping(ApiRoutes.POST.BY_ID)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") UUID id);

    @Operation(description = "Get post replies by ID")
    @ApiResponse(responseCode = "404", description = "ID not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping(ApiRoutes.POST.REPLIES)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Paged<PostDTO>> getRepliesByPostId(
        @PathVariable("id") UUID id,
        @Valid PageParameters pageParams);

}
