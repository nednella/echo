package com.example.echo_api.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.controller.auth.AuthController;
import com.example.echo_api.exception.custom.badrequest.UsernameAlreadyExistsException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.request.auth.LoginDTO;
import com.example.echo_api.persistence.dto.request.auth.SignupDTO;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.service.auth.onboarding.OnboardingService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link AuthController}.
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OnboardingService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void AuthController_Login_Return204() throws Exception {
        // api: POST /api/v1/auth/login ==> 204 : No Content
        String path = ApiConfig.Auth.LOGIN;

        LoginDTO request = new LoginDTO(
            "admin",
            "password");

        String body = objectMapper.writeValueAsString(request);

        doNothing()
            .when(authService)
            .login(request);

        mockMvc.perform(
            post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void AuthController_Login_Throw400InvalidRequest_InvalidUsername() throws Exception {
        // api: POST /api/v1/auth/login ==> 400 : Invalid Request
        String path = ApiConfig.Auth.LOGIN;

        LoginDTO request = new LoginDTO(
            null,
            "valid-pw-1");

        String body = objectMapper.writeValueAsString(request);

        String response = mockMvc
            .perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            "Username is required.",
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AuthController_Login_Throw400InvalidRequest_InvalidPassword() throws Exception {
        // api: POST /api/v1/auth/login ==> 400 : Invalid Request
        String path = ApiConfig.Auth.LOGIN;

        LoginDTO request = new LoginDTO(
            "valid_name",
            null);

        String body = objectMapper.writeValueAsString(request);

        String response = mockMvc
            .perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            "Password is required.",
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AuthController_Login_Throw404ResourceNotFound() throws Exception {
        // api: POST /api/v1/auth/login ==> 404 : ResourceNotFound
        String path = ApiConfig.Auth.LOGIN;

        LoginDTO request = new LoginDTO(
            "admin",
            "password");

        String body = objectMapper.writeValueAsString(request);

        doThrow(new ResourceNotFoundException())
            .when(authService)
            .login(request);

        String response = mockMvc.perform(
            post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AuthController_Login_Throw400BadCredentials() throws Exception {
        // api: POST /api/v1/auth/login ==> 400 : BadCredentials
        String path = ApiConfig.Auth.LOGIN;

        LoginDTO request = new LoginDTO(
            "admin",
            "password");

        String body = objectMapper.writeValueAsString(request);

        doThrow(new BadCredentialsException(""))
            .when(authService)
            .login(request);

        String response = mockMvc.perform(
            post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.USERNAME_OR_PASSWORD_INCORRECT,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AuthController_Login_Throw401AccountStatusDisabled() throws Exception {
        // api: POST /api/v1/auth/login ==> 401 : AccountStatus - Disabled
        String path = ApiConfig.Auth.LOGIN;

        LoginDTO request = new LoginDTO(
            "admin",
            "password");

        String body = objectMapper.writeValueAsString(request);

        doThrow(new DisabledException("Account is disabled."))
            .when(authService)
            .login(request);

        String response = mockMvc.perform(
            post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.UNAUTHORIZED,
            ErrorMessageConfig.Unauthorised.ACCOUNT_STATUS,
            "Account is disabled.",
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AuthController_Login_Throw401AccountStatusLocked() throws Exception {
        // api: POST /api/v1/auth/login ==> 401 : AccountStatus - Locked
        String path = ApiConfig.Auth.LOGIN;

        LoginDTO request = new LoginDTO(
            "admin",
            "password");

        String body = objectMapper.writeValueAsString(request);

        doThrow(new LockedException("Account is locked."))
            .when(authService)
            .login(request);

        String response = mockMvc.perform(
            post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.UNAUTHORIZED,
            ErrorMessageConfig.Unauthorised.ACCOUNT_STATUS,
            "Account is locked.",
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AuthController_Signup_Return204() throws Exception {
        // api: POST /api/v1/auth/signup ==> 204 : No Content
        String path = ApiConfig.Auth.SIGNUP;

        SignupDTO request = new SignupDTO(
            "admin",
            "password-1");

        String body = objectMapper.writeValueAsString(request);

        doNothing()
            .when(authService)
            .signup(request);

        mockMvc.perform(
            post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void AuthController_Signup_Throw400InvalidRequest_InvalidUsername() throws Exception {
        // api: POST /api/v1/auth/signup ==> 400 : Invalid Request
        String path = ApiConfig.Auth.SIGNUP;

        SignupDTO request = new SignupDTO(
            "invalid_name!",
            "valid-pw-1");

        String body = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(
            post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_USERNAME,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AuthController_Signup_Throw400InvalidRequest_InvalidPassword() throws Exception {
        // api: POST /api/v1/auth/signup ==> 400 : Invalid Request
        String path = ApiConfig.Auth.SIGNUP;

        SignupDTO request = new SignupDTO(
            "valid_name",
            "invalid_password");

        String body = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(
            post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_PASSWORD,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

    @Test
    void AuthController_Signup_Throw400UsernameAlreadyExists() throws Exception {
        // api: POST /api/v1/auth/signup ==> 400 : UsernameAlreadyExists
        String path = ApiConfig.Auth.SIGNUP;

        SignupDTO request = new SignupDTO(
            "taken_name",
            "valid-pw-1");

        String body = objectMapper.writeValueAsString(request);

        doThrow(new UsernameAlreadyExistsException())
            .when(authService)
            .signup(request);

        String response = mockMvc.perform(
            post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.USERNAME_ARLEADY_EXISTS,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
    }

}
