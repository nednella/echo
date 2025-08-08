package com.example.echo_api.service.user;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.badrequest.ClerkIdAlreadyExistsException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.request.webhook.clerk.data.UserDeleted;
import com.example.echo_api.persistence.dto.request.webhook.clerk.data.UserUpdated;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing CRUD operations of {@link User} entities.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public User createUserWithProfile(String clerkId, String username, String imageUrl) {
        if (userRepository.existsByClerkId(clerkId)) {
            throw new ClerkIdAlreadyExistsException();
        }

        User user = User.fromClerk(clerkId, username);
        userRepository.save(user);

        Profile profile = Profile.fromClerk(user.getId(), user.getUsername(), imageUrl);
        profileRepository.save(profile);

        return user;
    }

    @Override
    public void handleClerkUserUpdated(UserUpdated data) {
        User user = getUserEntityByClerkId(data.id());
        Profile profile = getProfileEntityById(user.getId());

        user.setUsername(data.username());
        profile.setAvatarImageUrl(data.imageUrl());

        userRepository.save(user);
        profileRepository.save(profile);
    }

    @Override
    public void handleClerkUserDeleted(UserDeleted data) {
        userRepository.deleteByClerkId(data.id());
    }

    /**
     * Retrieve a {@link User} entity by its associated Clerk ID.
     * 
     * @param clerkId the Clerk ID of the user
     * @return The {@link User} entity
     * @throws ResourceNotFoundException If no user by that Clerk ID exists
     */
    private User getUserEntityByClerkId(String clerkId) throws ResourceNotFoundException {
        return userRepository.findByClerkId(clerkId)
            .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Retrieve a {@link Profile} via its {@link UUID}.
     * 
     * @param id The UUID of the profile
     * @return The {@link Profile} entity
     * @throws ResourceNotFoundException If no profile by that id exists
     */
    private Profile getProfileEntityById(UUID id) throws ResourceNotFoundException {
        return profileRepository.findById(id)
            .orElseThrow(ResourceNotFoundException::new);
    }

}
