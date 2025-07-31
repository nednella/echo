package com.example.echo_api.service.auth;

import com.example.echo_api.exception.custom.badrequest.OnboardingCompleteException;
import com.example.echo_api.exception.custom.internalserver.ClerkException;
import com.example.echo_api.persistence.model.user.User;

public interface AuthService {

    public User onboard() throws ClerkException, OnboardingCompleteException;

}
