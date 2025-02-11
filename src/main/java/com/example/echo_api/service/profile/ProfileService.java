package com.example.echo_api.service.profile;

import com.example.echo_api.exception.custom.username.UsernameNotFoundException;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
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
