package com.example.echo_api.controller.feed;

import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.service.feed.FeedService;
import com.example.echo_api.util.pagination.OffsetLimitRequest;
import com.example.echo_api.validation.pagination.annotations.Limit;
import com.example.echo_api.validation.pagination.annotations.Offset;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    // @formatter:off
    @GetMapping(ApiConfig.Feed.HOMEPAGE)
    public ResponseEntity<PageDTO<PostDTO>> getHomepage(
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(feedService.getHomepage(page));
    }
    // @formatter:on

    // @formatter:off
    @GetMapping(ApiConfig.Feed.DISCOVER)
    public ResponseEntity<PageDTO<PostDTO>> getDiscover(
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(feedService.getDiscover(page));
    }
    // @formatter:on

    // @formatter:off
    @GetMapping(ApiConfig.Feed.PROFILE_BY_USERNAME)
    public ResponseEntity<PageDTO<PostDTO>> getProfileByUsername(
        @PathVariable("username") String username,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(feedService.getProfile(username, page));
    }
    // @formatter:on

    // @formatter:off
    @GetMapping(ApiConfig.Feed.PROFILE_REPLIES_BY_ID)
    public ResponseEntity<PageDTO<PostDTO>> getProfileRepliesById(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(feedService.getProfileReplies(id, page));
    }
    // @formatter:on

    // @formatter:off
    @GetMapping(ApiConfig.Feed.PROFILE_LIKES_BY_ID)
    public ResponseEntity<PageDTO<PostDTO>> getProfileLikesById(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(feedService.getProfileLikes(id, page));
    }
    // @formatter:on

    // @formatter:off
    @GetMapping(ApiConfig.Feed.PROFILE_MENTIONS_BY_ID)
    public ResponseEntity<PageDTO<PostDTO>> getProfileMentionsById(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(feedService.getProfileMentions(id, page));
    }
    // @formatter:on

}
