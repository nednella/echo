package com.example.echo_api.controller.post;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.service.post.view.PostViewService;
import com.example.echo_api.util.OffsetLimitRequest;
import com.example.echo_api.validation.pagination.annotations.Limit;
import com.example.echo_api.validation.pagination.annotations.Offset;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@RestController
@RequiredArgsConstructor
public class PostViewController {

    private final PostViewService postViewService;

    @GetMapping(ApiRoutes.POST.BY_ID)
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(postViewService.getPostById(id));
    }

    // @formatter:off
    @GetMapping(ApiRoutes.POST.REPLIES)
    public ResponseEntity<PageDTO<PostDTO>> getRepliesById(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(postViewService.getRepliesById(id, page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiRoutes.FEED.HOMEPAGE)
    public ResponseEntity<PageDTO<PostDTO>> getHomepagePosts(
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit)
    {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(postViewService.getHomepagePosts(page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiRoutes.FEED.DISCOVER)
    public ResponseEntity<PageDTO<PostDTO>> getDiscoverPosts(
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(postViewService.getDiscoverPosts(page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiRoutes.FEED.POSTS)
    public ResponseEntity<PageDTO<PostDTO>> getPostsByProfileId(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(postViewService.getPostsByAuthorId(id, page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiRoutes.FEED.REPLIES)
    public ResponseEntity<PageDTO<PostDTO>> getRepliesByProfileId(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(postViewService.getRepliesByAuthorId(id, page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiRoutes.FEED.LIKES)
    public ResponseEntity<PageDTO<PostDTO>> getLikesByProfileId(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(postViewService.getLikesByAuthorId(id, page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiRoutes.FEED.MENTIONS)
    public ResponseEntity<PageDTO<PostDTO>> getMentionsOfProfileId(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(postViewService.getMentionsOfAuthorId(id, page));
    } // @formatter:on

}
