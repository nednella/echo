package com.example.echo_api.modules.feed.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.feed.api.FeedAPI;
import com.example.echo_api.modules.feed.service.FeedService;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.shared.pagination.OffsetLimitRequest;
import com.example.echo_api.shared.pagination.PageParameters;
import com.example.echo_api.shared.pagination.Paged;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class FeedController implements FeedAPI {

    private final FeedService feedService;

    @Override
    public ResponseEntity<Paged<PostDTO>> getHomeFeed(PageParameters pageParams) {
        Pageable page = OffsetLimitRequest.of(pageParams.getOffset(), pageParams.getLimit());
        return ResponseEntity.ok(feedService.getHomeFeed(page));
    }

    @Override
    public ResponseEntity<Paged<PostDTO>> getDiscoverFeed(PageParameters pageParams) {
        Pageable page = OffsetLimitRequest.of(pageParams.getOffset(), pageParams.getLimit());
        return ResponseEntity.ok(feedService.getDiscoverFeed(page));
    }

    @Override
    public ResponseEntity<Paged<PostDTO>> getProfilePosts(UUID id, PageParameters pageParams) {
        Pageable page = OffsetLimitRequest.of(pageParams.getOffset(), pageParams.getLimit());
        return ResponseEntity.ok(feedService.getProfilePosts(id, page));
    }

    @Override
    public ResponseEntity<Paged<PostDTO>> getProfileReplies(UUID id, PageParameters pageParams) {
        Pageable page = OffsetLimitRequest.of(pageParams.getOffset(), pageParams.getLimit());
        return ResponseEntity.ok(feedService.getProfileReplies(id, page));
    }

    @Override
    public ResponseEntity<Paged<PostDTO>> getProfileLikes(UUID id, PageParameters pageParams) {
        Pageable page = OffsetLimitRequest.of(pageParams.getOffset(), pageParams.getLimit());
        return ResponseEntity.ok(feedService.getProfileLikes(id, page));
    }

    @Override
    public ResponseEntity<Paged<PostDTO>> getProfileMentions(UUID id, PageParameters pageParams) {
        Pageable page = OffsetLimitRequest.of(pageParams.getOffset(), pageParams.getLimit());
        return ResponseEntity.ok(feedService.getProfileMentions(id, page));
    }

}
