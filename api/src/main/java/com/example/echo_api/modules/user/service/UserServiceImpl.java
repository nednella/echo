package com.example.echo_api.modules.user.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.modules.user.entity.User;
import com.example.echo_api.modules.user.repository.UserRepository;
import com.example.echo_api.util.Utils;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing CRUD operations of a {@link User} and its
 * associated {@link Profile}.
 */
@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public User upsertFromExternalSource(String externalId, String username, String imageUrl) {
        Utils.checkNotNull(externalId, "External ID");
        Utils.checkNotNull(username, "Username");

        Optional<User> user = userRepository.findByExternalId(externalId);

        return user.isPresent()
            ? updateExisting(user.get(), username, imageUrl)
            : createNew(externalId, username, imageUrl);
    }

    @Override
    @Transactional
    public int deleteFromExternalSource(String externalId) {
        return userRepository.deleteByExternalId(externalId);
    }

    /**
     * Create a new {@link User} and its associated {@link Profile} based on data
     * from an external identity provider (IDP).
     * 
     * @param externalId the unique identifier from the IDP
     * @param username   the unique username from the IDP
     * @param imageUrl   the profile image URL from the IDP
     * @return the new {@link User} instance
     * @throws IllegalArgumentException if {@code externalId} or {@code username} is
     *                                  null
     */
    private User createNew(String externalId, String username, String imageUrl) {
        User user = User.fromExternalSource(externalId);
        user = userRepository.save(user);

        Profile profile = Profile.forUser(user.getId());
        profile.setUsername(username);
        profile.setImageUrl(imageUrl);
        profile = profileRepository.save(profile);

        return user;
    }

    /**
     * Updates an existing {@link User}'s associated {@link Profile} with the
     * provided data from an external identity provider (IDP).
     * 
     * @param user     the existing {@link User} whose information is to be updated
     * @param username the new username to set (if changed) from the IDP
     * @param imageUrl the new image URL to set (if changed) from the IDP
     * @return the affected {@link User}
     * @throws IllegalArgumentException if {@code username} is null
     */
    private User updateExisting(User user, String username, String imageUrl) {
        profileRepository.findById(user.getId())
            .ifPresent(profile -> {
                boolean changed = false;

                if (!profile.getUsername().equals(username)) { // never null
                    profile.setUsername(username);
                    changed = true;
                }

                if (profile.getImageUrl() == null || !profile.getImageUrl().equals(imageUrl)) { // can be null
                    profile.setImageUrl(imageUrl);
                    changed = true;
                }

                if (changed) {
                    profileRepository.save(profile);
                }
            });

        return user;
    }

}
