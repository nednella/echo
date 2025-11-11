package com.example.echo_api.modules.profile.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.echo_api.modules.profile.dto.response.ProfileDTO;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.modules.profile.entity.ProfileFollow;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.user.entity.User;
import com.example.echo_api.modules.user.repository.UserRepository;
import com.example.echo_api.shared.pagination.OffsetLimitRequest;
import com.example.echo_api.testing.support.AbstractRepositoryTest;

// TODO: test coverage for profile metrics

/**
 * Integration test class for {@link ProfileRepository}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileRepositoryIT extends AbstractRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileFollowRepository profileFollowRepository;

    private Profile source;
    private Profile target;

    @BeforeAll
    void setup() {
        User sourceU = User.fromExternalSource("placeholderExtId1");
        User targetU = User.fromExternalSource("placeholderExtId2");
        userRepository.saveAll(List.of(sourceU, targetU));

        source = Profile.forTest(sourceU.getId(), "source");
        target = Profile.forTest(targetU.getId(), "target");
        profileRepository.saveAll(List.of(source, target));

        // save a follow to db
        ProfileFollow follow = new ProfileFollow(source.getId(), target.getId());
        profileFollowRepository.save(follow);
    }

    @Test
    void findByUsername_ReturnsProfileDto_WhenProfileByUsernameExists() {
        Optional<Profile> result = profileRepository.findByUsername(source.getUsername());

        assertThat(result).isNotNull().isPresent();
    }

    @Test
    void findByUsername_ReturnsOptionalEmpty_WhenProfileByUsernameDoesNotExist() {
        Optional<Profile> result = profileRepository.findByUsername("non-existent-username");

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void findProfileDtoById_ReturnsProfileDto_WhenProfileByIdExists() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoById(target.getId(), source.getId());

        assertThat(result).isNotNull().isPresent();
    }

    @Test
    void findProfileDtoById_ReturnsOptionalEmpty_WhenProfileByIdDoesNotExist() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoById(UUID.randomUUID(), source.getId());

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void findProfileDtoById_ReturnsProfileDtoWithNullRelationshipDto_WhenProfileByIdIsSelf() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoById(source.getId(), source.getId());

        assertThat(result).isNotNull().isPresent();
        assertThat(result.get().relationship()).isNull();
    }

    @Test
    void findProfileDtoById_ReturnsProfileDtoWithNonNullRelationshipDto_WhenProfileByIdIsNotSelf() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoById(target.getId(), source.getId());

        assertThat(result).isNotNull().isPresent();
        assertThat(result.get().relationship()).isNotNull();
    }

    @Test
    void findProfileDtoByUsername_ReturnsProfileDto_WhenProfileByUsernameExists() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoByUsername(target.getUsername(), source.getId());

        assertThat(result).isNotNull().isPresent();
    }

    @Test
    void findProfileDtoByUsername_ReturnsOptionalEmpty_WhenProfileByUsernameDoesNotExist() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoByUsername("non-existent", source.getId());

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void findProfileDtoByUsername_ReturnsProfileDtoWithNullRelationshipDTO_WhenProfileByUsernameIsSelf() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoByUsername(source.getUsername(), source.getId());

        assertThat(result).isNotNull().isPresent();
        assertThat(result.get().relationship()).isNull();
    }

    @Test
    void findProfileDtoByUsername_ReturnsProfileDtoWithNonNullRelationshipDTO_WhenProfileByUsernameIsNotSelf() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoByUsername(target.getUsername(), source.getId());

        assertThat(result).isNotNull().isPresent();
        assertThat(result.get().relationship()).isNotNull();
    }

    @Test
    void findFollowerDtosById_ReturnsPageDtoOfProfileDto_WhenProfileByIdHasFollowers() {
        Pageable page = OffsetLimitRequest.of(0, 10);

        Page<SimplifiedProfileDTO> followersPage = profileRepository.findFollowerDtosById(
            target.getId(),
            source.getId(),
            page);

        assertThat(followersPage).isNotNull();
        assertThat(followersPage.getContent()).isNotEmpty();
    }

    @Test
    void findFollowerDtosById_ReturnsPageDtoOfEmpty_WhenProfileByIdHasNoFollowers() {
        Pageable page = OffsetLimitRequest.of(0, 10);

        Page<SimplifiedProfileDTO> followersPage = profileRepository.findFollowerDtosById(
            source.getId(),
            source.getId(),
            page);

        assertThat(followersPage).isNotNull();
        assertThat(followersPage.getContent()).isEmpty();
    }

    @Test
    void findFollowingDtosById_ReturnsPageDtoOfProfileDto_WhenProfileByIdIsFollowingOthers() {
        Pageable page = OffsetLimitRequest.of(0, 10);

        Page<SimplifiedProfileDTO> followingPage = profileRepository.findFollowingDtosById(
            source.getId(),
            source.getId(),
            page);

        assertThat(followingPage).isNotNull();
        assertThat(followingPage.getContent()).isNotEmpty();
    }

    @Test
    void findFollowingDtosById_ReturnsPageDtoOfEmpty_WhenProfileByIdIsNotFollowingOthers() {
        Pageable page = OffsetLimitRequest.of(0, 10);

        Page<SimplifiedProfileDTO> followingPage = profileRepository.findFollowingDtosById(
            target.getId(),
            source.getId(),
            page);

        assertThat(followingPage).isNotNull();
        assertThat(followingPage.getContent()).isEmpty();
    }

}
