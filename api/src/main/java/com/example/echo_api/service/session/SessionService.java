package com.example.echo_api.service.session;

import com.example.echo_api.persistence.model.account.Account;

public interface SessionService {

    /**
     * 
     */
    public Account getAuthenticatedUser();

}
