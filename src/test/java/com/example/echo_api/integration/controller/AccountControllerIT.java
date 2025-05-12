package com.example.echo_api.integration.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpMethod.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.controller.auth.AuthController;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.integration.util.TestUtils;
import com.example.echo_api.persistence.dto.request.account.UpdatePasswordDTO;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;

/**
 * Integration test class for {@link AuthController}.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AccountControllerIT extends IntegrationTest {

    @Test
    void AccountController_UsernameAvailable_Return200True() {
        // api: GET /api/v1/account/username-available?username={...} ==> 200 : True
        String path = ApiConfig.Account.USERNAME_AVAILABLE + "?username=" + "unique_name_123";

        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertEquals("true", response.getBody());
    }

    @Test
    void AccountController_UsernameAvailable_Return200False() {
        // api: GET /api/v1/account/username-available?username={...} ==> 200 : False
        String path = ApiConfig.Account.USERNAME_AVAILABLE + "?username=" + existingAccount.getUsername();

        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertEquals("false", response.getBody());
    }

    @Test
    @Transactional // rollback
    void AccountController_UpdateUsername_Return204() {
        // api: PUT /api/v1/account/username ==> 204 : No Content
        String putPath = ApiConfig.Account.UPDATE_USERNAME + "?username=" + "new_username";

        // PUT
        ResponseEntity<Void> putResponse = restTemplate.exchange(putPath, PUT, null, Void.class);
        assertEquals(NO_CONTENT, putResponse.getStatusCode());

        // GET (assert db)
        String getPath = ApiConfig.Account.USERNAME_AVAILABLE + "?username=" + "new_username";
        ResponseEntity<String> getResponse = restTemplate.getForEntity(getPath, String.class);
        assertEquals(OK, getResponse.getStatusCode());
        assertEquals("false", getResponse.getBody());
    }

    @Test
    void AccountController_UpdateUsername_Return400UsernameAlreadyExists() {
        // api: PUT /api/v1/account/username ==> 400 : UsernameAlreadyExists
        String path = ApiConfig.Account.UPDATE_USERNAME + "?username=" + existingAccount.getUsername();

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(path, PUT, null, ErrorDTO.class);

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.BadRequest.USERNAME_ARLEADY_EXISTS, error.message());
    }

    @Test
    @Transactional // rollback
    void AccountController_UpdatePassword_Return204() {
        // api: PUT /api/v1/account/password ==> 204 : No Content
        String path = ApiConfig.Account.UPDATE_PASSWORD;
        UpdatePasswordDTO update = new UpdatePasswordDTO(existingAccount.getPassword(), "new-pw1", "new-pw1");

        // PUT 1
        HttpEntity<UpdatePasswordDTO> request = TestUtils.createJsonRequestEntity(update);
        ResponseEntity<Void> response1 = restTemplate.exchange(path, PUT, request, Void.class);
        assertEquals(NO_CONTENT, response1.getStatusCode());

        // PUT 2 (assert db)
        ResponseEntity<ErrorDTO> response2 = restTemplate.exchange(path, PUT, request, ErrorDTO.class);
        assertEquals(BAD_REQUEST, response2.getStatusCode());
        assertNotNull(response2.getBody());

        ErrorDTO error = response2.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.BadRequest.INCORRECT_CURRENT_PASSWORD, error.message());
    }

    @Test
    void AccountController_UpdatePassword_Return400IncorrectCurrentPassword() {
        // api: PUT /api/v1/account/password ==> 400 : IncorrectCurrentPassword
        String path = ApiConfig.Account.UPDATE_PASSWORD;
        UpdatePasswordDTO update = new UpdatePasswordDTO("wrong-password", "new-pw1", "new-pw1");

        HttpEntity<UpdatePasswordDTO> request = TestUtils.createJsonRequestEntity(update);
        ResponseEntity<ErrorDTO> response = restTemplate.exchange(path, PUT, request, ErrorDTO.class);

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.BadRequest.INCORRECT_CURRENT_PASSWORD, error.message());
    }

}
