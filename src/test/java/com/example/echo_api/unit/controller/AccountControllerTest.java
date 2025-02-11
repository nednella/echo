package com.example.echo_api.unit.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.controller.account.AccountController;
import com.example.echo_api.exception.custom.password.IncorrectCurrentPasswordException;
import com.example.echo_api.exception.custom.username.UsernameAlreadyExistsException;
import com.example.echo_api.persistence.dto.request.account.UpdatePasswordDTO;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.service.account.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link AccountController}.
 */
@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private Account testAccount;

    /**
     * Sets up a {@link Account} object before each test.
     */
    @BeforeEach
    public void setup() {
        testAccount = new Account(
            "testUsername",
            "testPassword");
    }

    @Test
    void AccountController_IsUsernameAvailable_Return200True() throws Exception {
        // api: GET /api/v1/account/username-available?username={...} ==> 200 : True
        String path = ApiConfig.Account.USERNAME_AVAILABLE;

        when(accountService.isUsernameAvailable(testAccount.getUsername()))
            .thenReturn(true);

        String response = mockMvc
            .perform(get(path)
                .param("username", testAccount.getUsername()))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertEquals("true", response);
    }

    @Test
    void AccountController_IsUsernameAvailable_Return200False() throws Exception {
        // api: GET /api/v1/account/username-available?username={...} ==> 200 : False
        String path = ApiConfig.Account.USERNAME_AVAILABLE;

        when(accountService.isUsernameAvailable(testAccount.getUsername()))
            .thenReturn(false);

        String response = mockMvc
            .perform(get(path)
                .param("username", testAccount.getUsername()))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertEquals("false", response);
    }

    @Test
    void AccountController_UpdateUsername_Return204() throws Exception {
        // api: PUT /api/v1/account/username ==> 204 : No Content
        String path = ApiConfig.Account.UPDATE_USERNAME;

        doNothing().when(accountService).updateUsername(testAccount.getUsername());

        mockMvc
            .perform(put(path)
                .param("username", testAccount.getUsername()))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void AccountController_UpdateUsername_Throw400UsernameAlreadyExists() throws Exception {
        // api: PUT /api/v1/account/username ==> 400 : UsernameAlreadyExists
        String path = ApiConfig.Account.UPDATE_USERNAME;

        doThrow(new UsernameAlreadyExistsException()).when(accountService).updateUsername(testAccount.getUsername());

        String response = mockMvc
            .perform(put(path)
                .param("username", testAccount.getUsername()))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.USERNAME_ARLEADY_EXISTS,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AccountController_UpdatePassword_Return204() throws Exception {
        // api: PUT /api/v1/account/password ==> 204 : No Content
        String path = ApiConfig.Account.UPDATE_PASSWORD;

        UpdatePasswordDTO request = new UpdatePasswordDTO(
            "current_password",
            "new-password-1",
            "new-password-1");

        String body = objectMapper.writeValueAsString(request);

        doNothing().when(accountService).updatePassword(request.currentPassword(), request.newPassword());

        mockMvc
            .perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void AccountController_UpdatePassword_Throw400InvalidRequest_InvalidPassword() throws Exception {
        // api: PUT /api/v1/account/password ==> 400 : Invalid Request (new password)
        String path = ApiConfig.Account.UPDATE_PASSWORD;

        UpdatePasswordDTO request = new UpdatePasswordDTO(
            "old-password",
            "new-password",
            "new-password");

        String body = objectMapper.writeValueAsString(request);

        String response = mockMvc
            .perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_PASSWORD,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AccountController_UpdatePassword_Throw400InvalidRequest_ConfirmationPasswordMismatch() throws Exception {
        // api: PUT /api/v1/account/password ==> 400 : Invalid Request (confirmation
        // password)
        String path = ApiConfig.Account.UPDATE_PASSWORD;

        UpdatePasswordDTO request = new UpdatePasswordDTO(
            "old-password",
            "new-password-1",
            "this-does-not-match");

        String body = objectMapper.writeValueAsString(request);

        String response = mockMvc
            .perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.INVALID_REQUEST,
            ValidationMessageConfig.CONFIRMATION_PASSWORD_MISMATCH,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AccountController_UpdatePassword_Throw400InvalidRequest_NewPasswordNotUnique() throws Exception {
        // api: PUT /api/v1/account/password ==> 400 : Invalid Request (new password)
        String path = ApiConfig.Account.UPDATE_PASSWORD;

        UpdatePasswordDTO request = new UpdatePasswordDTO(
            "old-password-1",
            "old-password-1",
            "old-password-1");

        String body = objectMapper.writeValueAsString(request);

        String response = mockMvc
            .perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.INVALID_REQUEST,
            ValidationMessageConfig.NEW_PASSWORD_NOT_UNIQUE,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AccountController_UpdatePassword_Throw400IncorrectCurrentPassword() throws Exception {
        // api: PUT /api/v1/account/password ==> 400 : IncorrectCurrentPassword
        String path = ApiConfig.Account.UPDATE_PASSWORD;

        UpdatePasswordDTO request = new UpdatePasswordDTO(
            "wrong-password",
            "new-password-1",
            "new-password-1");

        String body = objectMapper.writeValueAsString(request);

        doThrow(new IncorrectCurrentPasswordException())
            .when(accountService)
            .updatePassword(request.currentPassword(), request.newPassword());

        String response = mockMvc
            .perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.INCORRECT_CURRENT_PASSWORD,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

}
