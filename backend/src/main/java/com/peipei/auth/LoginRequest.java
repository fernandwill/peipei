package com.peipei.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Login payload (dev step 3, §38).
 */
public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password) {
}
