package app.echo_social.modules.profile.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import app.echo_social.modules.profile.entity.Profile;

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

    /**
     * Returns whether a {@link Profile} with the given {@code username} exists.
     * 
     * @param username
     * @return {@code true} if exists, else {@code false}
     */
    boolean existsByUsername(String username);

}
