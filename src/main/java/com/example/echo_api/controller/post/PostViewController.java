package com.example.echo_api.controller.post;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.service.post.view.PostViewService;
import com.example.echo_api.util.pagination.OffsetLimitRequest;
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

    @GetMapping(ApiConfig.Post.GET_BY_ID)
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(postViewService.getPostById(id));
    }

    // @formatter:off
    @GetMapping(ApiConfig.Post.GET_REPLIES_BY_ID)
    public ResponseEntity<PageDTO<PostDTO>> getPostRepliesById(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(postViewService.getPostRepliesById(id, page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiConfig.Feed.HOMEPAGE)
    public ResponseEntity<PageDTO<PostDTO>> getHomepagePosts(
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit)
    {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(postViewService.getHomepagePosts(page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiConfig.Feed.DISCOVER)
    public ResponseEntity<PageDTO<PostDTO>> getDiscoverPosts(
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(postViewService.getDiscoverPosts(page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiConfig.Feed.PROFILE_BY_USERNAME)
    public ResponseEntity<PageDTO<PostDTO>> getProfilePostsByUsername(
        @PathVariable("username") String username,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(postViewService.getProfilePostsByUsername(username, page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiConfig.Feed.PROFILE_REPLIES_BY_ID)
    public ResponseEntity<PageDTO<PostDTO>> getProfileRepliesById(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(postViewService.getProfileRepliesById(id, page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiConfig.Feed.PROFILE_LIKES_BY_ID)
    public ResponseEntity<PageDTO<PostDTO>> getProfileLikesById(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(postViewService.getProfileLikesById(id, page));
    } // @formatter:on

    // @formatter:off
    @GetMapping(ApiConfig.Feed.PROFILE_MENTIONS_BY_ID)
    public ResponseEntity<PageDTO<PostDTO>> getProfileMentionsById(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(postViewService.getProfileMentionsById(id, page));
    } // @formatter:on

}
