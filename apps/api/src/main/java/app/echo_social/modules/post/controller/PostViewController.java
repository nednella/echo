package app.echo_social.modules.post.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import app.echo_social.modules.post.api.PostViewAPI;
import app.echo_social.modules.post.dto.response.PostDTO;
import app.echo_social.modules.post.service.PostViewService;
import app.echo_social.shared.pagination.OffsetLimitRequest;
import app.echo_social.shared.pagination.PageParameters;
import app.echo_social.shared.pagination.Paged;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class PostViewController implements PostViewAPI {

    private final PostViewService postViewService;

    @Override
    public ResponseEntity<PostDTO> getPostById(UUID id) {
        return ResponseEntity.ok(postViewService.getPostById(id));
    }

    @Override
    public ResponseEntity<Paged<PostDTO>> getRepliesByPostId(UUID id, PageParameters pageParams) {
        Pageable page = OffsetLimitRequest.of(pageParams.getOffset(), pageParams.getLimit());
        return ResponseEntity.ok(postViewService.getRepliesByPostId(id, page));
    }

}
