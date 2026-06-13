package app.echo_social.modules.clerk.dto;

import java.util.Map;

import org.springframework.lang.NonNull;

import app.echo_social.util.Utils;

/**
 * Standardised representation of a Clerk user object.
 */
public record ClerkUser(
    @NonNull String id,
    @NonNull String username,
    String externalId,
    String imageUrl,
    @NonNull Map<String, Object> publicMetadata
) {

    public ClerkUser {
        Utils.checkNotNull(id, "ClerkUser id");
        Utils.checkNotNull(username, "ClerkUser username");
        Utils.checkNotNull(publicMetadata, "ClerkUser public_metadata");
    }

}
