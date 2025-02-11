package com.example.echo_api.persistence.repository;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.follow.FollowPK;

public interface FollowRepository extends ListCrudRepository<Follow, FollowPK> {

    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

}
