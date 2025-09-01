package com.example.echo_api.modules.clerk.mapper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import com.clerk.backend_api.models.components.User;
import com.example.echo_api.modules.clerk.dto.ClerkUser;
import com.example.echo_api.modules.clerk.dto.webhook.UserUpsert;

/**
 * Unit test class for {@link ClerkUserMapper}.
 */
class ClerkUserMapperTest {

    private User createMockClerkSdkUser(
        Optional<String> id,
        JsonNullable<String> username,
        JsonNullable<String> externalId,
        Optional<String> imageUrl,
        Optional<Map<String, Object>> metadata) {
        return User.builder()
            .id(id)
            .username(username)
            .externalId(externalId)
            .imageUrl(imageUrl)
            .publicMetadata(metadata)
            .build();
    }

    private UserUpsert createMockUserUpsert(
        String id,
        String username,
        String externalId,
        String imageUrl,
        Map<String, Object> publicMetadata) {
        return new UserUpsert(id, username, externalId, imageUrl, publicMetadata);
    }

    @Test
    void fromSDK_ReturnsClerkUser() {
        var id = Optional.of("id");
        var username = JsonNullable.of("username");
        var externalId = JsonNullable.of("externalId");
        var imgUrl = Optional.of("imgUrl");
        Optional<Map<String, Object>> metadata = Optional.of(new HashMap<>());

        User sdkUser = createMockClerkSdkUser(id, username, externalId, imgUrl, metadata);

        ClerkUser user = assertDoesNotThrow(() -> ClerkUserMapper.fromSDK(sdkUser));
        assertNotNull(user.id());
        assertNotNull(user.username());
        assertNotNull(user.publicMetadata());
    }

    @Test
    void fromSDK_ThrowsWhenIdIsEmpty() {
        Optional<String> id = Optional.empty();
        var username = JsonNullable.of("username");
        var externalId = JsonNullable.of("externalId");
        var imgUrl = Optional.of("imgUrl");
        Optional<Map<String, Object>> metadata = Optional.of(new HashMap<>());

        User user = createMockClerkSdkUser(id, username, externalId, imgUrl, metadata);

        assertThrows(IllegalArgumentException.class, () -> ClerkUserMapper.fromSDK(user));
    }

    @Test
    void fromSDK_ThrowsWhenUsernameIsEmpty() {
        var id = Optional.of("id");
        JsonNullable<String> username = JsonNullable.undefined();
        var externalId = JsonNullable.of("externalId");
        var imgUrl = Optional.of("imgUrl");
        Optional<Map<String, Object>> metadata = Optional.of(new HashMap<>());

        User user = createMockClerkSdkUser(id, username, externalId, imgUrl, metadata);

        assertThrows(IllegalArgumentException.class, () -> ClerkUserMapper.fromSDK(user));
    }

    @Test
    void fromSDK_ThrowsWhenPublicMetadataIsEmpty() {
        var id = Optional.of("id");
        var username = JsonNullable.of("username");
        JsonNullable<String> externalId = JsonNullable.of("externalId");
        var imgUrl = Optional.of("imgUrl");
        Optional<Map<String, Object>> metadata = Optional.empty();

        User user = createMockClerkSdkUser(id, username, externalId, imgUrl, metadata);

        assertThrows(IllegalArgumentException.class, () -> ClerkUserMapper.fromSDK(user));
    }

    @Test
    void fromWebhook_ReturnsClerkUser() {
        String id = "id";
        String username = "username";
        Map<String, Object> metadata = new HashMap<>();
        UserUpsert upsert = createMockUserUpsert(id, username, null, null, metadata);

        ClerkUser user = assertDoesNotThrow(() -> ClerkUserMapper.fromWebhook(upsert));
        assertNotNull(user.id());
        assertNotNull(user.username());
        assertNotNull(user.publicMetadata());
    }

    @Test
    void fromWebhook_ThrowsWhenIdIsNull() {
        String id = null;
        UserUpsert upsert = createMockUserUpsert(id, "username", null, null, new HashMap<>());

        assertThrows(IllegalArgumentException.class, () -> ClerkUserMapper.fromWebhook(upsert));
    }

    @Test
    void fromWebhook_ThrowsWhenUsernameIsNull() {
        String username = null;
        UserUpsert upsert = createMockUserUpsert("id", username, null, null, new HashMap<>());

        assertThrows(IllegalArgumentException.class, () -> ClerkUserMapper.fromWebhook(upsert));
    }

    @Test
    void fromWebhook_ThrowsWhenPublicMetadataIsNull() {
        Map<String, Object> metadata = null;
        UserUpsert upsert = createMockUserUpsert("id", "username", null, null, metadata);

        assertThrows(IllegalArgumentException.class, () -> ClerkUserMapper.fromWebhook(upsert));
    }

}
