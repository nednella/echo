package com.example.echo_api.persistence.repository;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.example.echo_api.persistence.model.post.entity.PostEntity;

public interface PostMentionRepository extends ListCrudRepository<PostEntity, UUID> {

}
