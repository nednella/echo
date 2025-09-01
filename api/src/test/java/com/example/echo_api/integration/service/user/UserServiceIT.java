package com.example.echo_api.integration.service.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.persistence.repository.UserRepository;
import com.example.echo_api.service.user.UserServiceImpl;

/**
 * Integration test class for {@link UserServiceImpl}.
 */
@Import(UserServiceImpl.class)
class UserServiceIT extends RepositoryTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    /**
     * Create and persist a {@link User} and {@link Profile} in db to assert against
     * existing in tests.
     * 
     * @param externalId the external ID of the user to persist
     * @param username   the username of the user to persist
     * @return the persisted {@link User}
     */
    private User createUser(String externalId, String username, String imageUrl) {
        User user = User.fromExternalSource(externalId);
        user = userRepository.save(user);

        Profile profile = Profile.forUser(user.getId());
        profile.setUsername(username);
        profile.setImageUrl(imageUrl);
        profileRepository.save(profile);

        return user;
    }

    @Test
    void upsertFromExternalSource_CreatesUserAndProfile_WhenNewExternalUser() {
        // arrange
        String externalId = "user_SomeUniqueId";
        String username = "username";

        // act
        User user = userService.upsertFromExternalSource(externalId, username, null);

        // assert
        assertThat(user.getExternalId()).isEqualTo(externalId);

        var expectedUser = userRepository.findByExternalId(externalId).orElseThrow();
        assertThat(expectedUser.getId()).isEqualTo(user.getId());

        var expectedProfile = profileRepository.findById(user.getId()).orElseThrow();
        assertThat(expectedProfile.getUsername()).isEqualTo(username);
    }

    @Test
    void upsertFromExternalSource_OnlyUpdatesExistingProfile_WhenExistingExternalUser() {
        // arrange
        String externalId = "user_SomeUniqueId";
        String username = "username";
        String imageUrl = "imageUrl";
        User expected = createUser(externalId, username, imageUrl);

        // act
        String newUsername = "RoboticAlien4";
        String newImageUrl = null;
        User actual = userService.upsertFromExternalSource(externalId, newUsername, newImageUrl);

        // assert
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getExternalId()).isEqualTo(expected.getExternalId());

        var profile = profileRepository.findById(expected.getId()).orElseThrow();
        assertThat(profile.getUsername()).isEqualTo(newUsername);
        assertThat(profile.getImageUrl()).isNull();

    }

}
