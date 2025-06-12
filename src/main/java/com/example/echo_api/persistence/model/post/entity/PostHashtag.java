package com.example.echo_api.persistence.model.post.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "post_hashtag")
public class PostHashtag extends PostEntity {

    public PostHashtag(UUID postId, int start, int end, String text) {
        super(postId, start, end, text);
    }

}
