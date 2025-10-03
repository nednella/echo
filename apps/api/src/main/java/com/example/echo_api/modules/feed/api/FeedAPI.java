package com.example.echo_api.modules.feed.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.PageParameters;
import com.example.echo_api.shared.pagination.Paged;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Feed API")
@Validated
public interface FeedAPI {

    @GetMapping(ApiRoutes.FEED.HOMEPAGE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getHomeFeed(@Valid PageParameters pageParams);

    @GetMapping(ApiRoutes.FEED.DISCOVER)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getDiscoverFeed(@Valid PageParameters pageParams);

    @GetMapping(ApiRoutes.FEED.POSTS)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getProfilePosts(@PathVariable("id") UUID id, @Valid PageParameters pageParams);

    @GetMapping(ApiRoutes.FEED.REPLIES)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getProfileReplies(@PathVariable("id") UUID id, @Valid PageParameters pageParams);

    @GetMapping(ApiRoutes.FEED.LIKES)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getProfileLikes(@PathVariable("id") UUID id, @Valid PageParameters pageParams);

    @GetMapping(ApiRoutes.FEED.MENTIONS)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getProfileMentions(@PathVariable("id") UUID id, @Valid PageParameters pageParams);

}
