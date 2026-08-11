package com.peipei.auth;

import com.peipei.common.ApiException;
import com.peipei.common.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

/**
 * Registration, login, token refresh (with rotation), and logout (dev steps 3-4, §38).
 */
@Service
public class AuthService {

    private final UserRepository users;
    private final RefreshTokenRepository refreshTokens;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final String dummyPasswordHash;

    public AuthService(UserRepository users, RefreshTokenRepository refreshTokens,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users = users;
        this.refreshTokens = refreshTokens;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        // Pre-computed BCrypt hash used to equalize login timing for unknown accounts.
        this.dummyPasswordHash = passwordEncoder.encode("peipei-timing-equalizer");
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (users.existsByEmail(email)) {
            throw new ApiException(ErrorCode.EMAIL_ALREADY_REGISTERED, "An account with this email already exists");
        }
        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            // BCrypt silently truncates at 72 bytes; reject up front so behavior is predictable.
            throw new ApiException(ErrorCode.VALIDATION_ERROR, "password must not exceed 72 bytes");
        }
        User user = new User(email, passwordEncoder.encode(request.password()), UserRole.MERCHANT);
        try {
            users.save(user);
        } catch (DataIntegrityViolationException ex) {
            // Concurrent registration with the same email: the DB unique constraint wins the race.
            throw new ApiException(ErrorCode.EMAIL_ALREADY_REGISTERED, "An account with this email already exists");
        }
        return issueTokens(user);
    }

    private AuthResponse issueTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        refreshTokens.save(new RefreshToken(user, hash(refreshToken), Instant.now().plus(jwtService.refreshTtl())));
        return new AuthResponse(accessToken, refreshToken, jwtService.accessTtl().toSeconds(), UserView.from(user));
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private static String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 not available", ex);
        }
    }
}
