package com.example.echo_api.modules.clerk.mapper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import com.clerk.backend_api.models.components.User;
import com.example.echo_api.modules.clerk.dto.sdk.ClerkUserDTO;

/**
 * Unit test class for {@link ClerkUserMapper}.
 */
class ClerkUserMapperTest {

    private User createMockClerkUser(
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

    @Test
    void toDTO_ReturnsClerkUserDTO() {
        var id = Optional.of("id");
        var username = JsonNullable.of("username");
        var externalId = JsonNullable.of("externalId");
        var imgUrl = Optional.of("imgUrl");
        Optional<Map<String, Object>> metadata = Optional.of(new HashMap<>());

        User user = createMockClerkUser(id, username, externalId, imgUrl, metadata);

        ClerkUserDTO userDTO = assertDoesNotThrow(() -> ClerkUserMapper.toDTO(user));
        assertNotNull(userDTO.id());
        assertNotNull(userDTO.username());
        assertNotNull(userDTO.publicMetadata());
    }

    @Test
    void toDTO_ThrowsWhenIdIsEmpty() {
        Optional<String> id = Optional.empty();
        var username = JsonNullable.of("username");
        var externalId = JsonNullable.of("externalId");
        var imgUrl = Optional.of("imgUrl");
        Optional<Map<String, Object>> metadata = Optional.of(new HashMap<>());

        User user = createMockClerkUser(id, username, externalId, imgUrl, metadata);

        assertThrows(IllegalArgumentException.class, () -> ClerkUserMapper.toDTO(user));
    }

    @Test
    void toDTO_ThrowsWhenUsernameIsEmpty() {
        var id = Optional.of("id");
        JsonNullable<String> username = JsonNullable.undefined();
        var externalId = JsonNullable.of("externalId");
        var imgUrl = Optional.of("imgUrl");
        Optional<Map<String, Object>> metadata = Optional.of(new HashMap<>());

        User user = createMockClerkUser(id, username, externalId, imgUrl, metadata);

        assertThrows(IllegalArgumentException.class, () -> ClerkUserMapper.toDTO(user));
    }

    @Test
    void toDTO_ThrowsWhenPublicMetadataIsEmpty() {
        var id = Optional.of("id");
        var username = JsonNullable.of("username");
        JsonNullable<String> externalId = JsonNullable.of("externalId");
        var imgUrl = Optional.of("imgUrl");
        Optional<Map<String, Object>> metadata = Optional.empty();

        User user = createMockClerkUser(id, username, externalId, imgUrl, metadata);

        assertThrows(IllegalArgumentException.class, () -> ClerkUserMapper.toDTO(user));
    }

    @Test
    void toListDTO_ReturnsListOfClerkUserDTO() {
        var id = Optional.of("id");
        var username = JsonNullable.of("username");
        JsonNullable<String> externalId = JsonNullable.of("externalId");
        var imgUrl = Optional.of("imgUrl");
        Optional<Map<String, Object>> metadata = Optional.of(new HashMap<>());

        User user1 = createMockClerkUser(id, username, externalId, imgUrl, metadata);
        User user2 = createMockClerkUser(id, username, externalId, imgUrl, metadata);
        List<User> users = List.of(user1, user2);

        List<ClerkUserDTO> userDTOs = assertDoesNotThrow(() -> ClerkUserMapper.toListDTO(users));
        for (var userDTO : userDTOs) {
            assertNotNull(userDTO.id());
            assertNotNull(userDTO.username());
            assertNotNull(userDTO.publicMetadata());
        }
    }

}
