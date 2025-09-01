package com.example.echo_api.modules.clerk.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.modules.clerk.dto.ClerkUser;
import com.example.echo_api.modules.user.entity.User;
import com.example.echo_api.modules.user.service.UserService;

/**
 * Unit test class for {@link ClerkSyncService}.
 */
@ExtendWith(MockitoExtension.class)
class ClerkSyncServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private ClerkSyncServiceImpl clerkSyncService;

    @Test
    void ingestUserUpserted_ReturnsUser_ForAllCases() {
        // arrange
        UUID id = UUID.randomUUID();
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        Map<String, Object> metadata = new HashMap<>();
        ClerkUser clerkUser = new ClerkUser(externalId, username, null, imageUrl, metadata);
        User expected = User.forTest(id, externalId);
        when(userService.upsertFromExternalSource(externalId, username, imageUrl)).thenReturn(expected);

        // act
        User actual = clerkSyncService.ingestUserUpserted(clerkUser);

        // assert
        assertThat(actual).isEqualTo(expected);
        verify(userService).upsertFromExternalSource(externalId, username, imageUrl);
    }

    @Test
    void ingestUserDeleted_ReturnsVoid_ForAllCases() {
        // arrange
        String externalId = "user_someRandomStringThatIsUniqueApparently";

        // act & assert
        assertDoesNotThrow(() -> clerkSyncService.ingestUserDeleted(externalId));
        verify(userService).deleteFromExternalSource(externalId);
    }

}
