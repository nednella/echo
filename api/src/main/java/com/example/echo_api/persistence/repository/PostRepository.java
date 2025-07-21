package com.example.echo_api.persistence.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.repository.custom.CustomPostRepository;

@Repository
public interface PostRepository
    extends CrudRepository<Post, UUID>, PagingAndSortingRepository<Post, UUID>, CustomPostRepository {

}
