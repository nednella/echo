package com.example.echo_api.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.custom.CustomProfileRepository;

public interface ProfileRepository
    extends CrudRepository<Profile, UUID>, PagingAndSortingRepository<Profile, UUID>, CustomProfileRepository {

    /**
     * Find a {@link Profile} by {@code username}.
     * 
     * @param username The username to search for.
     * @return An {@link Optional} containing the {@link Profile} if found,
     *         otherwise empty.
     */
    Optional<Profile> findByUsername(String username);

}
