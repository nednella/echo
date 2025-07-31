package com.example.echo_api.service.clerk;

import com.clerk.backend_api.models.components.User;
import com.example.echo_api.exception.custom.internalserver.ClerkException;

public interface ClerkService {

    public User getUser(String clerkUserId) throws ClerkException;

    public void setExternalId(String clerkUserId, String externalId) throws ClerkException;

    public void completeOnboarding(String clerkUserId) throws ClerkException;

}
