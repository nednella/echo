package com.example.echo_api.integration.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpMethod.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

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
        String path = ApiConfig.Account.USERNAME_AVAILABLE + "?username=" + otherUser.getUsername();

        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertEquals("false", response.getBody());
    }

    @Test
    void AccountController_UpdateUsername_Return204NoContent() {
        // api: PUT /api/v1/account/username ==> 204 : No Content
        String putPath = ApiConfig.Account.UPDATE_USERNAME + "?username=" + "new_username";

        ResponseEntity<Void> response = restTemplate.exchange(putPath, PUT, null, Void.class);

        // assert response
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void AccountController_UpdateUsername_Return400UsernameAlreadyExists() {
        // api: PUT /api/v1/account/username ==> 400 : UsernameAlreadyExists
        String path = ApiConfig.Account.UPDATE_USERNAME + "?username=" + otherUser.getUsername();

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
    void AccountController_UpdatePassword_Return204NoContent() {
        // api: PUT /api/v1/account/password ==> 204 : No Content
        String path = ApiConfig.Account.UPDATE_PASSWORD;

        UpdatePasswordDTO update = new UpdatePasswordDTO(TEST_ENV_PASSWORD, "new-pw1", "new-pw1");
        HttpEntity<UpdatePasswordDTO> request = TestUtils.createJsonRequestEntity(update);

        ResponseEntity<Void> response = restTemplate.exchange(path, PUT, request, Void.class);

        // assert response
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
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
