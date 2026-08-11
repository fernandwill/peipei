package com.peipei.auth;

/**
 * Lightweight principal placed in the security context by {@link JwtAuthFilter}. It is built
 * from JWT claims rather than a database entity, so authenticated requests never hit the
 * database just to identify the caller.
 */
public record AuthenticatedUser(Long id, String email, UserRole role) {
}
