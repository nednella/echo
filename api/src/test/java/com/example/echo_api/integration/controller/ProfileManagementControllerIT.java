package com.example.echo_api.integration.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpMethod.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.integration.util.TestUtils;
import com.example.echo_api.persistence.dto.request.profile.UpdateInformationDTO;

/**
 * Integration test class for {@link ProfileManagementController}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProfileManagementControllerIT extends IntegrationTest {

    @Test
    void ProfileController_UpdateProfile_Return204NoContent() {
        // api: PUT /api/v1/profile/me/info ==> 204 : No Content
        String path = ApiConfig.Profile.ME_INFO;

        UpdateInformationDTO body = new UpdateInformationDTO(
            "name",
            "bio",
            "location");

        HttpEntity<UpdateInformationDTO> request = TestUtils.createJsonRequestEntity(body);
        ResponseEntity<Void> response = restTemplate.exchange(path, PUT, request, Void.class);

        // assert response
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

}
