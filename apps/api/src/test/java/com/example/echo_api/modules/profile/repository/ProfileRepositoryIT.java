package com.example.echo_api.modules.profile.repository;

import static org.junit.jupiter.api.Assertions.*;

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

/**
 * TODO
 * 
 * 1. Improve test method names
 * 2. Adopt assertj assertThat usage
 * 3. Add additional test coverage if possible
 * 
 */

// TODO: SQL query testing for metrics/relationships

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
    void ProfileRepository_FindByUsername_ReturnProfile() {
        Optional<Profile> foundProfile = profileRepository.findByUsername(source.getUsername());

        assertNotNull(foundProfile);
        assertTrue(foundProfile.isPresent());
    }

    @Test
    void ProfileRepository_FindByUsername_ReturnEmpty() {
        Optional<Profile> foundProfile = profileRepository.findByUsername("non-existent-username");

        assertNotNull(foundProfile);
        assertTrue(foundProfile.isEmpty());
    }

    @Test
    void ProfileRepository_FindProfileDtoById_ReturnProfileDto() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoById(target.getId(), source.getId());

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().relationship());
    }

    @Test
    void ProfileRepository_FindProfileDtoById_ReturnProfileDtoWithNullRelationshipDto() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoById(source.getId(), source.getId());

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNull(result.get().relationship());
    }

    @Test
    void ProfileRepository_FindProfileDtoById_ReturnEmpty() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoById(UUID.randomUUID(), source.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void ProfileRepository_FindProfileDtoByUsername_ReturnProfileDto() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoByUsername(target.getUsername(), source.getId());

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().relationship());
    }

    @Test
    void ProfileRepository_FindProfileDtoByUsername_ReturnProfileDtoWithNullRelationshipDto() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoByUsername(source.getUsername(), source.getId());

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNull(result.get().relationship());
    }

    @Test
    void ProfileRepository_FindProfileDtoByUsername_ReturnEmpty() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoByUsername("non-existent", source.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void ProfileRepository_FindFollowerDtosById_ReturnPageOfProfileDto() {
        Pageable page = OffsetLimitRequest.of(0, 10);

        Page<SimplifiedProfileDTO> followersPage = profileRepository.findFollowerDtosById(
            target.getId(),
            source.getId(),
            page);

        assertNotNull(followersPage);
        assertTrue(followersPage.hasContent());
        assertEquals(1, followersPage.getTotalElements());
    }

    @Test
    void ProfileRepository_FindFollowerDtosById_ReturnPageOfEmpty() {
        Pageable page = OffsetLimitRequest.of(0, 10);

        Page<SimplifiedProfileDTO> followersPage = profileRepository.findFollowerDtosById(
            source.getId(),
            source.getId(),
            page);

        assertNotNull(followersPage);
        assertTrue(followersPage.isEmpty());
        assertEquals(0, followersPage.getTotalElements());
    }

    @Test
    void ProfileRepository_FindFollowingDtosById_ReturnPageOfProfileDto() {
        Pageable page = OffsetLimitRequest.of(0, 10);

        Page<SimplifiedProfileDTO> followingPage = profileRepository.findFollowingDtosById(
            source.getId(),
            source.getId(),
            page);

        assertNotNull(followingPage);
        assertTrue(followingPage.hasContent());
        assertEquals(1, followingPage.getTotalElements());
    }

    @Test
    void ProfileRepository_FindFollowingDtosById_ReturnPageOfEmpty() {
        Pageable page = OffsetLimitRequest.of(0, 10);

        Page<SimplifiedProfileDTO> followingPage = profileRepository.findFollowingDtosById(
            target.getId(),
            source.getId(),
            page);

        assertNotNull(followingPage);
        assertTrue(followingPage.isEmpty());
        assertEquals(0, followingPage.getTotalElements());
    }

}
