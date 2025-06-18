package com.example.echo_api.persistence.model.post.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "post_mention")
public class PostMention extends PostEntity {

    public PostMention(UUID postId, int start, int end, String text) {
        super(postId, start, end, text);
    }

}
