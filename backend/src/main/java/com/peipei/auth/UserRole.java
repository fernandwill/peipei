package com.peipei.auth;

/**
 * Roles a registered user can hold. {@code ADMIN} is reserved for platform operators and the
 * tenant-isolation rules of later dev steps.
 */
public enum UserRole {
    MERCHANT,
    ADMIN
}
