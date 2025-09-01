package com.example.echo_api.modules.profile.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.echo_api.modules.profile.entity.Profile;

@Repository
public interface ProfileRepository
    extends CrudRepository<Profile, UUID>, PagingAndSortingRepository<Profile, UUID>, CustomProfileRepository {

    /**
     * Find a {@link Profile} by {@code username}.
     * 
     * @param username the username to search for
     * @return {@link Optional} containing {@link Profile} if found, else empty
     */
    Optional<Profile> findByUsername(String username);

}
