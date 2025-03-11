package com.example.echo_api.service.profile;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.exception.custom.cloudinary.CloudinaryException;
import com.example.echo_api.exception.custom.image.ImageException;
import com.example.echo_api.exception.custom.username.UsernameNotFoundException;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;

public interface ProfileService {

    /**
     * Fetch a {@link Profile} by username to return to the client.
     * 
     * @param username The username of the profile to fetch.
     * @return A {@link ProfileDTO} resembling the profile.
     * @throws UsernameNotFoundException If the username is not found.
     */
    public ProfileDTO getByUsername(String username) throws UsernameNotFoundException;

    /**
     * Fetches the {@link Profile} of the authenticated user to return to the
     * client.
     * 
     * @return A {@link ProfileDTO} resembling the profile.
     */
    public ProfileDTO getMe();

    /**
     * Updates the profile information of the authenticated user.
     * 
     * <p>
     * Valid fields are {@code name}, {@code bio}, {@code location}.
     * 
     * @param request The {@link UpdateProfileDTO} request containing the updated
     *                profile information.
     */
    public void updateMeProfile(UpdateProfileDTO request);

    /**
     * Updates the profile avatar image of the authenticated user.
     * 
     * @param image The {@link MultipartFile} resembling the uploaded image.
     * @throws ImageException      If there is an existing avatar and its
     *                             {@link Image} entity could not be found when
     *                             deleting.
     * @throws CloudinaryException If there was an error when interacting with the
     *                             Cloudinary SDK.
     */
    public void updateMeAvatar(MultipartFile image) throws ImageException, CloudinaryException;

    /**
     * Removes the profile avatar image of the authenticated user.
     * 
     * @throws ImageException      If the existing avatar's {@link Image} entity
     *                             could not be found when deleting.
     * @throws CloudinaryException If there was an error when interacting with the
     *                             Cloudinary SDK.
     */
    public void deleteMeAvatar() throws ImageException, CloudinaryException;

    /**
     * Updates the profile banner image of the authenticated user.
     * 
     * @param image The {@link MultipartFile} resembling the uploaded image.
     * @throws ImageException      If there is an existing banner and its
     *                             {@link Image} entity could not be found when
     *                             deleting.
     * @throws CloudinaryException If there was an error when interacting with the
     *                             Cloudinary SDK.
     */
    public void updateMeBanner(MultipartFile image) throws ImageException, CloudinaryException;

    /**
     * Removes the profile avatar image of the authenticated user.
     * 
     * @throws ImageException      If the existing banner's {@link Image} entity
     *                             could not be found when deleting.
     * @throws CloudinaryException If there was an error when interacting with the
     *                             Cloudinary SDK.
     */
    public void deleteMeBanner() throws ImageException, CloudinaryException;

    /**
     * Fetches a {@link List} of {@link Profile} for the followers list of the
     * supplied {@code username} and {@code page} parameters.
     * 
     * @param username The username of the profile to search against.
     * @param page     The {@link Pageable} containing the pagination parameters.
     * @return A {@link List} of {@link Profile} for matches, otherwise empty.
     * @throws UsernameNotFoundException If the username is not found.
     */
    public PageDTO<ProfileDTO> getFollowers(String username, Pageable page) throws UsernameNotFoundException;

    /**
     * Fetches a {@link List} of {@link Profile} for the following list of the
     * supplied {@code username} and {@code page} parameters.
     * 
     * @param username The username of the profile to search against.
     * @param page     The {@link Pageable} containing the pagination parameters.
     * @return A {@link List} of {@link Profile} for matches, otherwise empty.
     * @throws UsernameNotFoundException If the username is not found.
     */
    public PageDTO<ProfileDTO> getFollowing(String username, Pageable page) throws UsernameNotFoundException;

    /**
     * Create a {@link Follow} relationship between the authenticated profile and
     * the target profile.
     * 
     * @param username The username of the target profile.
     * @throws UsernameNotFoundException If the username is not found.
     */
    public void follow(String username) throws UsernameNotFoundException;

    /**
     * Delete a {@link Follow} relationship between the authenticated profile and
     * the target profile.
     * 
     * @param username The username of the target profile.
     * @throws UsernameNotFoundException If the username is not found.
     */
    public void unfollow(String username) throws UsernameNotFoundException;

    /**
     * Create a {@link Block} relationship between the authenticated profile and the
     * target profile.
     * 
     * @param username The username of the target profile.
     * @throws UsernameNotFoundException If the username is not found.
     */
    public void block(String username) throws UsernameNotFoundException;

    /**
     * Delete a {@link Block} relationship between the authenticated profile and the
     * target profile.
     * 
     * @param username The username of the target profile.
     * @throws UsernameNotFoundException If the username is not found.
     */
    public void unblock(String username) throws UsernameNotFoundException;

}
