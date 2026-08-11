package com.peipei.auth;

/**
 * Token pair returned on register/login/refresh (dev step 3, §38).
 */
public record AuthResponse(String accessToken, String refreshToken, long expiresInSeconds, UserView user) {
}
