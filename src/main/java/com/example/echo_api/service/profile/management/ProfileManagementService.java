package com.example.echo_api.service.profile.management;

import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.exception.custom.cloudinary.CloudinaryException;
import com.example.echo_api.exception.custom.image.ImageException;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.model.image.Image;

public interface ProfileManagementService {

    /**
     * Updates the profile information of the authenticated user.
     * 
     * <p>
     * Valid fields are {@code name}, {@code bio}, {@code location}.
     * 
     * @param request The request DTO containing the updated profile information.
     */
    public void updateInformation(UpdateProfileDTO request);

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
    public void updateAvatar(MultipartFile image) throws ImageException, CloudinaryException;

    /**
     * Removes the profile avatar image of the authenticated user.
     * 
     * @throws ImageException      If the existing avatar's {@link Image} entity
     *                             could not be found when deleting.
     * @throws CloudinaryException If there was an error when interacting with the
     *                             Cloudinary SDK.
     */
    public void deleteAvatar() throws ImageException, CloudinaryException;

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
    public void updateBanner(MultipartFile image) throws ImageException, CloudinaryException;

    /**
     * Removes the profile avatar image of the authenticated user.
     * 
     * @throws ImageException      If the existing banner's {@link Image} entity
     *                             could not be found when deleting.
     * @throws CloudinaryException If there was an error when interacting with the
     *                             Cloudinary SDK.
     */
    public void deleteBanner() throws ImageException, CloudinaryException;

}
