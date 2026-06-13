package app.echo_social.modules.feed.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import app.echo_social.modules.feed.api.FeedAPI;
import app.echo_social.modules.feed.service.FeedService;
import app.echo_social.modules.post.dto.response.PostDTO;
import app.echo_social.shared.pagination.OffsetLimitRequest;
import app.echo_social.shared.pagination.PageParameters;
import app.echo_social.shared.pagination.Paged;

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
