package com.example.echo_api.persistence.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.echo_api.persistence.model.post.Post;

public interface PostRepository extends CrudRepository<Post, UUID>, PagingAndSortingRepository<Post, UUID> {

}
