package com.example.echo_api.loader;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.repository.AccountRepository;
import com.example.echo_api.service.account.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountLoader extends AbstractJsonLoader<Account> {

    private static final String PATH = "data/dev-users.json";

    private final AccountRepository accountRepository;

    private final AccountService accountService;

    @Override
    protected String getFilePath() {
        return PATH;
    }

    @Override
    protected void loadData() throws IOException {
        if (accountRepository.findAll().isEmpty()) {
            log.info("ACCOUNT table is empty. Populating table from path: {}", getFilePath());
            List<Account> entities = loadJsonFromResourceFile(getFilePath(), Account.class);
            saveToRepository(entities);
        } else {
            log.info("ACCOUNT table is not empty. Skipping data population.");
        }
    }

    @Override
    protected void saveToRepository(List<Account> entities) {
        for (Account account : entities) {
            try {
                accountService.registerWithRole(account.getUsername(), account.getPassword(), account.getRole());
            } catch (Exception ex) {
                log.error("Failed to create account: {}. Reason: {}", account.getUsername(), ex.getMessage(), ex);
            }
        }
    }

}
