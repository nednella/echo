package com.example.echo_api.service.clerk.sdk;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clerk.backend_api.Clerk;
import com.clerk.backend_api.models.components.User;
import com.clerk.backend_api.models.operations.GetUserResponse;
import com.clerk.backend_api.models.operations.UpdateUserRequestBody;
import com.example.echo_api.config.ClerkConfig;
import com.example.echo_api.exception.custom.internalserver.ClerkException;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for interacting with the {@link Clerk} SDK.
 * 
 * @see https://github.com/clerk/clerk-sdk-java
 */
@Service
@RequiredArgsConstructor
public class ClerkSdkServiceImpl implements ClerkSdkService {

    private static final String GET_USER_EXCEPTION_MSG = "Failed to retrieve user data. Try again and contact support if this persists.";
    private static final String SET_EXTERNAL_ID_EXCEPTION_MSG = "Failed to update external ID. Try again and contact support if this persists.";
    private static final String COMPLETE_ONBOARDING_EXCEPTION_MSG = "Failed to update public metadata. Try again and contact support if this persists.";

    private final Clerk clerk;

    @Override
    public User getUser(String clerkUserId) throws ClerkException {
        try {
            GetUserResponse res = clerk.users().get(clerkUserId);
            return res.user().orElseThrow();
        } catch (Exception ex) {
            throw new ClerkException(GET_USER_EXCEPTION_MSG);
        }
    }

    @Override
    public void setExternalId(String clerkUserId, String externalId) throws ClerkException {
        try {
            clerk.users().update(
                clerkUserId,
                UpdateUserRequestBody.builder().externalId(externalId).build());
        } catch (Exception ex) {
            throw new ClerkException(SET_EXTERNAL_ID_EXCEPTION_MSG);
        }
    }

    @Override
    public void completeOnboarding(String clerkUserId) throws ClerkException {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ClerkConfig.ONBOARDING_COMPLETE_KEY, ClerkConfig.ONBOARDING_COMPLETE_VALUE);

        try {
            clerk.users().update(
                clerkUserId,
                UpdateUserRequestBody.builder().publicMetadata(metadata).build());
        } catch (Exception ex) {
            throw new ClerkException(COMPLETE_ONBOARDING_EXCEPTION_MSG);
        }
    }

}
