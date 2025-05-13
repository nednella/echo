package com.example.echo_api.service.profile.management;

import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.exception.custom.internalserver.CloudinaryException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.request.profile.UpdateInformationDTO;
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
    public void updateInformation(UpdateInformationDTO request);

    /**
     * Updates the profile avatar image of the authenticated user.
     * 
     * @param image The {@link MultipartFile} resembling the uploaded image.
     * @throws ResourceNotFoundException If there is an existing avatar and its
     *                                   {@link Image} entity could not be found
     *                                   when deleting.
     * @throws CloudinaryException       If there was an error when interacting with
     *                                   the Cloudinary SDK.
     */
    public void updateAvatar(MultipartFile image) throws ResourceNotFoundException, CloudinaryException;

    /**
     * Removes the profile avatar image of the authenticated user.
     * 
     * @throws ResourceNotFoundException If the existing avatar's {@link Image}
     *                                   entity could not be found when deleting.
     * @throws CloudinaryException       If there was an error when interacting with
     *                                   the Cloudinary SDK.
     */
    public void deleteAvatar() throws ResourceNotFoundException, CloudinaryException;

    /**
     * Updates the profile banner image of the authenticated user.
     * 
     * @param image The {@link MultipartFile} resembling the uploaded image.
     * @throws ResourceNotFoundException If there is an existing banner and its
     *                                   {@link Image} entity could not be found
     *                                   when deleting.
     * @throws CloudinaryException       If there was an error when interacting with
     *                                   the Cloudinary SDK.
     */
    public void updateBanner(MultipartFile image) throws ResourceNotFoundException, CloudinaryException;

    /**
     * Removes the profile avatar image of the authenticated user.
     * 
     * @throws ResourceNotFoundException If the existing banner's {@link Image}
     *                                   entity could not be found when deleting.
     * @throws CloudinaryException       If there was an error when interacting with
     *                                   the Cloudinary SDK.
     */
    public void deleteBanner() throws ResourceNotFoundException, CloudinaryException;

}
