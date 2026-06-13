package app.echo_social.modules.clerk.service;

import org.springframework.stereotype.Service;

import com.clerk.backend_api.Clerk;
import app.echo_social.modules.clerk.dto.ClerkUser;
import app.echo_social.modules.user.entity.User;
import app.echo_social.modules.user.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for maintaining local application synchronisation with
 * {@link Clerk}.
 */
@Service
@RequiredArgsConstructor
class ClerkSyncServiceImpl implements ClerkSyncService {

    private final UserService userService;

    @Override
    public User ingestUserUpserted(ClerkUser user) {
        return userService.upsertFromExternalSource(user.id(), user.username(), user.imageUrl());
    }

    @Override
    public void ingestUserDeleted(String clerkId) {
        userService.deleteFromExternalSource(clerkId);
    }

}