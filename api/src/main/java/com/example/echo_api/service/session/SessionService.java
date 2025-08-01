package com.example.echo_api.service.session;

import java.util.UUID;

public interface SessionService {

    public UUID getAuthenticatedUserId();

    public String getAuthenticatedUserClerkId();

}
