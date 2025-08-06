package com.example.echo_api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.badrequest.ClerkIdAlreadyExistsException;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing CRUD operations of {@link User} entities.
 */
@Service
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

        Profile profile = Profile.forUser(user.getId(), user.getUsername(), imageUrl);
        profileRepository.save(profile);

        return user;
    }

}
