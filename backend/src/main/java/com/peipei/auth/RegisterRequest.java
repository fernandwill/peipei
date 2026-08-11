package com.peipei.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Registration payload for the merchant dashboard (dev step 3, §38).
 */
public record RegisterRequest(
        @NotBlank @Email(message = "must be a valid email address") String email,
        @NotBlank @Size(min = 8, max = 72, message = "must be between 8 and 72 characters") String password) {
}
