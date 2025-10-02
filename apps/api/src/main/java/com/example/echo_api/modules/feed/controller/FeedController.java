package com.example.echo_api.modules.feed.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.feed.service.FeedService;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.OffsetLimitRequest;
import com.example.echo_api.shared.pagination.Paged;
import com.example.echo_api.shared.validation.annotations.Limit;
import com.example.echo_api.shared.validation.annotations.Offset;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Feed API")
@Validated
@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping(ApiRoutes.FEED.HOMEPAGE)
    public ResponseEntity<Paged<PostDTO>> getHomeFeed(
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(feedService.getHomeFeed(page));
    }

    @GetMapping(ApiRoutes.FEED.DISCOVER)
    public ResponseEntity<Paged<PostDTO>> getDiscoverFeed(
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(feedService.getDiscoverFeed(page));
    }

    @GetMapping(ApiRoutes.FEED.POSTS)
    public ResponseEntity<Paged<PostDTO>> getProfilePosts(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(feedService.getProfilePosts(id, page));
    }

    @GetMapping(ApiRoutes.FEED.REPLIES)
    public ResponseEntity<Paged<PostDTO>> getProfileReplies(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(feedService.getProfileReplies(id, page));
    }

    @GetMapping(ApiRoutes.FEED.LIKES)
    public ResponseEntity<Paged<PostDTO>> getProfileLikes(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(feedService.getProfileLikes(id, page));
    }

    @GetMapping(ApiRoutes.FEED.MENTIONS)
    public ResponseEntity<Paged<PostDTO>> getProfileMentions(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(feedService.getProfileMentions(id, page));
    }

}
