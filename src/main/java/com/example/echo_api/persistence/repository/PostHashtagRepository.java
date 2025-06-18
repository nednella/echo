package com.example.echo_api.persistence.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.example.echo_api.persistence.model.post.entity.PostEntity;
import com.example.echo_api.persistence.model.post.entity.PostEntityPK;

public interface PostHashtagRepository extends ListCrudRepository<PostEntity, PostEntityPK> {

}
