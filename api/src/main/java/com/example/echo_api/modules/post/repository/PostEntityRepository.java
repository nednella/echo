package com.example.echo_api.modules.post.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.example.echo_api.modules.post.entity.PostEntity;
import com.example.echo_api.modules.post.entity.PostEntityPK;

public interface PostEntityRepository extends ListCrudRepository<PostEntity, PostEntityPK> {}
