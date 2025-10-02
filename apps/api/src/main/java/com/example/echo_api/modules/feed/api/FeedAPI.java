package com.example.echo_api.modules.feed.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.Paged;
import com.example.echo_api.shared.validation.annotations.Limit;
import com.example.echo_api.shared.validation.annotations.Offset;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Feed API")
@Validated
public interface FeedAPI {

    @GetMapping(ApiRoutes.FEED.HOMEPAGE)
    ResponseEntity<Paged<PostDTO>> getHomeFeed(
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit);

    @GetMapping(ApiRoutes.FEED.DISCOVER)
    ResponseEntity<Paged<PostDTO>> getDiscoverFeed(
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit);

    @GetMapping(ApiRoutes.FEED.POSTS)
    ResponseEntity<Paged<PostDTO>> getProfilePosts(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit);

    @GetMapping(ApiRoutes.FEED.REPLIES)
    ResponseEntity<Paged<PostDTO>> getProfileReplies(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit);

    @GetMapping(ApiRoutes.FEED.LIKES)
    ResponseEntity<Paged<PostDTO>> getProfileLikes(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit);

    @GetMapping(ApiRoutes.FEED.MENTIONS)
    ResponseEntity<Paged<PostDTO>> getProfileMentions(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit);

}
