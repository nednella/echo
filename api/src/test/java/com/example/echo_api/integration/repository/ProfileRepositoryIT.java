package com.example.echo_api.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.persistence.repository.UserRepository;
import com.example.echo_api.util.pagination.OffsetLimitRequest;
import com.example.echo_api.persistence.repository.FollowRepository;

/**
 * Integration test class for {@link ProfileRepository}.
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProfileRepositoryIT extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private FollowRepository followRepository;

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
        Follow follow = new Follow(source.getId(), target.getId());
        followRepository.save(follow);
    }

    /**
     * Test {@link ProfileRepository#findByUsername(String)} to verify a profile can
     * be found by their username.
     */
    @Test
    void ProfileRepository_FindByUsername_ReturnProfile() {
        Optional<Profile> foundProfile = profileRepository.findByUsername(source.getUsername());

        assertNotNull(foundProfile);
        assertTrue(foundProfile.isPresent());
    }

    /**
     * Test {@link ProfileRepository#findByUsername(String)} to verify that
     * searching for a non-existent username returns an empty result.
     */
    @Test
    void ProfileRepository_FindByUsername_ReturnEmpty() {
        Optional<Profile> foundProfile = profileRepository.findByUsername("non-existent-username");

        assertNotNull(foundProfile);
        assertTrue(foundProfile.isEmpty());
    }

    /**
     * Test {@link ProfileRepository#findProfileDtoById(UUID, UUID)} to verify that
     * searching for user's {@link ProfileDTO} by {@code id} that IS NOT the
     * authenticated user, returns an {@link Optional} of {@link ProfileDTO}, which
     * includes relationship data between the target user and the authenticated
     * user.
     */
    @Test
    void ProfileRepository_FindProfileDtoById_ReturnProfileDto() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoById(target.getId(), source.getId());

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().relationship());
    }

    /**
     * Test {@link ProfileRepository#findProfileDtoById(UUID, UUID)} to verify that
     * searching for user's {@link ProfileDTO} by {@code id} that IS the
     * authenticated user, returns an {@link Optional} of {@link ProfileDTO}, minus
     * relationship data.
     */
    @Test
    void ProfileRepository_FindProfileDtoById_ReturnProfileDtoWithNullRelationshipDto() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoById(source.getId(), source.getId());

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNull(result.get().relationship());
    }

    /**
     * Test {@link ProfileRepository#findProfileDtoById(UUID, UUID)} to verify that
     * searching for user's {@link ProfileDTO} by a {@code id} that does not exist
     * returns an empty {@link Optional}.
     */
    @Test
    void ProfileRepository_FindProfileDtoById_ReturnEmpty() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoById(UUID.randomUUID(), source.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test {@link ProfileRepository#findProfileDtoByUsername(String, UUID)} to
     * verify that searching for user's {@link ProfileDTO} by {@code username} that
     * IS NOT the authenticated user, returns an {@link Optional} of
     * {@link ProfileDTO}, which includes relationship data between the target user
     * and the authenticated user.
     */
    @Test
    void ProfileRepository_FindProfileDtoByUsername_ReturnProfileDto() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoByUsername(target.getUsername(), source.getId());

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().relationship());
    }

    /**
     * Test {@link ProfileRepository#findProfileDtoByUsername(String, UUID)} to
     * verify that searching for user's {@link ProfileDTO} by {@code username} that
     * IS the authenticated user, returns an {@link Optional} of {@link ProfileDTO},
     * minus relationship data.
     */
    @Test
    void ProfileRepository_FindProfileDtoByUsername_ReturnProfileDtoWithNullRelationshipDto() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoByUsername(source.getUsername(), source.getId());

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNull(result.get().relationship());
    }

    /**
     * Test {@link ProfileRepository#findProfileDtoByUsername(String, UUID)} to
     * verify that searching for user's {@link ProfileDTO} by a {@code username}
     * that does not exist returns an empty {@link Optional}.
     */
    @Test
    void ProfileRepository_FindProfileDtoByUsername_ReturnEmpty() {
        Optional<ProfileDTO> result = profileRepository.findProfileDtoByUsername("non-existent", source.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test {@link ProfileRepository#findFollowerDtosById(UUID, UUID, Pageable)} to
     * verify that searching for a user's followers by their {@code id} returns a
     * {@link Page} of {@link ProfileDTO}.
     */
    @Test
    void ProfileRepository_FindFollowerDtosById_ReturnPageOfProfileDto() {
        Pageable page = new OffsetLimitRequest(0, 10);

        Page<SimplifiedProfileDTO> followersPage = profileRepository.findFollowerDtosById(
            target.getId(),
            source.getId(),
            page);

        assertNotNull(followersPage);
        assertTrue(followersPage.hasContent());
        assertEquals(1, followersPage.getTotalElements());
    }

    /**
     * Test {@link ProfileRepository#findFollowerDtosById(UUID, UUID, Pageable)} to
     * verify that searching for a user's followers by their {@code id}, that has no
     * followers, returns an empty {@link Page}.
     */
    @Test
    void ProfileRepository_FindFollowerDtosById_ReturnPageOfEmpty() {
        Pageable page = new OffsetLimitRequest(0, 10);

        Page<SimplifiedProfileDTO> followersPage = profileRepository.findFollowerDtosById(
            source.getId(),
            source.getId(),
            page);

        assertNotNull(followersPage);
        assertTrue(followersPage.isEmpty());
        assertEquals(0, followersPage.getTotalElements());
    }

    /**
     * Test {@link ProfileRepository#findFollowingDtosById(UUID, UUID, Pageable)} to
     * verify that searching for a user's following by their {@code id} returns a
     * {@link Page} of {@link ProfileDTO}.
     */
    @Test
    void ProfileRepository_FindFollowingDtosById_ReturnPageOfProfileDto() {
        Pageable page = new OffsetLimitRequest(0, 10);

        Page<SimplifiedProfileDTO> followingPage = profileRepository.findFollowingDtosById(
            source.getId(),
            source.getId(),
            page);

        assertNotNull(followingPage);
        assertTrue(followingPage.hasContent());
        assertEquals(1, followingPage.getTotalElements());
    }

    /**
     * Test {@link ProfileRepository#findFollowingDtosById(UUID, UUID, Pageable)} to
     * verify that searching for a user's following by their {@code id}, that is not
     * following any users, returns an empty {@link Page}.
     */
    @Test
    void ProfileRepository_FindFollowingDtosById_ReturnPageOfEmpty() {
        Pageable page = new OffsetLimitRequest(0, 10);

        Page<SimplifiedProfileDTO> followingPage = profileRepository.findFollowingDtosById(
            target.getId(),
            source.getId(),
            page);

        assertNotNull(followingPage);
        assertTrue(followingPage.isEmpty());
        assertEquals(0, followingPage.getTotalElements());
    }

}
