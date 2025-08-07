package com.example.echo_api.service.profile.management;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.exception.custom.internalserver.CloudinaryException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.request.profile.UpdateInformationDTO;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.auth.session.SessionService;
import com.example.echo_api.service.file.FileService;
import com.example.echo_api.service.profile.BaseProfileService;

/**
 * Service implementation for managing mutation operations for {@link Profile}
 * information related to the authenticated user.
 */
@Service
public class ProfileManagementServiceImpl extends BaseProfileService implements ProfileManagementService {

    private final FileService fileService;

    // @formatter:off
    public ProfileManagementServiceImpl(
        SessionService sessionService,
        ProfileRepository profileRepository,
        FileService fileService
    ) {
        super(sessionService, profileRepository);
        this.fileService = fileService;
    }
    // @formatter:on

    @Override
    public void updateInformation(UpdateInformationDTO request) {
        Profile me = getAuthenticatedUserProfile();
        me.setName(request.name());
        me.setBio(request.bio());
        me.setLocation(request.location());
        profileRepository.save(me);
    }

    @Override
    public void updateAvatar(MultipartFile image) throws ResourceNotFoundException, CloudinaryException {
        throw new UnsupportedOperationException();
        // TODO: delete and replace w/ frontend uploads via signed urls. only store urls
        // in db
    }

    @Override
    public void deleteAvatar() throws ResourceNotFoundException, CloudinaryException {
        throw new UnsupportedOperationException();
        // TODO: delete and replace w/ frontend uploads via signed urls. only store urls
        // in db
    }

    @Override
    public void updateBanner(MultipartFile image) throws ResourceNotFoundException, CloudinaryException {
        throw new UnsupportedOperationException();
        // TODO: delete and replace w/ frontend uploads via signed urls. only store urls
        // in db
    }

    @Override
    public void deleteBanner() throws ResourceNotFoundException, CloudinaryException {
        throw new UnsupportedOperationException();
        // TODO: delete and replace w/ frontend uploads via signed urls. only store urls
        // in db
    }

}
