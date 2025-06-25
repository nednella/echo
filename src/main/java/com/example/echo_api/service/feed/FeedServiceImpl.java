package com.example.echo_api.service.feed;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.persistence.model.post.Post;
import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing read-only {@link Post} data presentation
 * operations associated with {@link PageDTO} of post objects.
 */
@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    @Override
    public PageDTO<PostDTO> getHomepage(Pageable page) {
        throw new UnsupportedOperationException("Unimplemented method 'getHomepage'");
    }

    @Override
    public PageDTO<PostDTO> getDiscover(Pageable page) {
        throw new UnsupportedOperationException("Unimplemented method 'getDiscover'");
    }

    @Override
    public PageDTO<PostDTO> getProfile(String username, Pageable page) {
        throw new UnsupportedOperationException("Unimplemented method 'getProfile'");
    }

    @Override
    public PageDTO<PostDTO> getProfileReplies(UUID id, Pageable page) {
        throw new UnsupportedOperationException("Unimplemented method 'getProfileReplies'");
    }

    @Override
    public PageDTO<PostDTO> getProfileLikes(UUID id, Pageable page) {
        throw new UnsupportedOperationException("Unimplemented method 'getProfileLikes'");
    }

    @Override
    public PageDTO<PostDTO> getProfileMentions(UUID id, Pageable page) {
        throw new UnsupportedOperationException("Unimplemented method 'getProfileMentions'");
    }

}
