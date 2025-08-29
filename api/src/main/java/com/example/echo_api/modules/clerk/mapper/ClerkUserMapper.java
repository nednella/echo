package com.example.echo_api.modules.clerk.mapper;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import com.clerk.backend_api.models.components.User;
import com.example.echo_api.modules.clerk.dto.sdk.ClerkUserDTO;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ClerkUserMapper {

    public static ClerkUserDTO toDTO(User user) {
        try {
            return new ClerkUserDTO(
                user.id().orElseThrow(),
                user.username().get(),
                user.externalId().orElse(null),
                user.imageUrl().orElse(null),
                user.publicMetadata().orElseThrow());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid Clerk User object; a required field was null");
        }
    }

    public static List<ClerkUserDTO> toListDTO(List<User> users) {
        return users.stream()
            .map(ClerkUserMapper::toDTO)
            .toList();
    }

}
