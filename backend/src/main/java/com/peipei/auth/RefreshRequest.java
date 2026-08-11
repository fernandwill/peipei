package com.peipei.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload for refresh and logout (dev step 4, §38).
 */
public record RefreshRequest(@NotBlank String refreshToken) {
}
