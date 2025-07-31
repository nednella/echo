package com.example.echo_api.service.session;

import java.util.UUID;

import com.example.echo_api.exception.custom.badrequest.OnboardingIncompleteException;

public interface SessionService {

    public UUID getAuthenticatedUserId() throws OnboardingIncompleteException;

    public String getAuthenticatedUserClerkId();

}
