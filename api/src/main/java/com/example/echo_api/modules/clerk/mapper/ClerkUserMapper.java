package com.example.echo_api.modules.clerk.mapper;

import static lombok.AccessLevel.PRIVATE;

import com.clerk.backend_api.models.components.User;
import com.clerk.backend_api.utils.Utils;
import com.example.echo_api.modules.clerk.dto.ClerkUser;
import com.example.echo_api.modules.clerk.dto.webhook.UserUpsert;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ClerkUserMapper {

    public static ClerkUser fromSDK(User user) {
        Utils.checkNotNull(user, "Clerk SDK user");

        try {
            return new ClerkUser(
                user.id().orElseThrow(),
                user.username().get(),
                user.externalId().orElse(null),
                user.imageUrl().orElse(null),
                user.publicMetadata().orElseThrow());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to map Clerk SDK user; required field was null");
        }
    }

    public static ClerkUser fromWebhook(UserUpsert user) {
        Utils.checkNotNull(user, "Clerk webhook user");

        return new ClerkUser(
            user.id(),
            user.username(),
            user.externalId(),
            user.imageUrl(),
            user.publicMetadata());
    }

}
