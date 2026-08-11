package com.peipei.auth;

/**
 * Safe, serializable view of a user for API responses — never exposes the password hash.
 */
public record UserView(Long id, String email, UserRole role) {

    static UserView from(User user) {
        return new UserView(user.getId(), user.getEmail(), user.getRole());
    }

    static UserView from(AuthenticatedUser user) {
        return new UserView(user.id(), user.email(), user.role());
    }
}
