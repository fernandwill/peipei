package com.peipei.auth;

import com.peipei.common.ApiException;
import com.peipei.common.ErrorCode;
import io.jsonwebtoken.JwtException;
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

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        User user = users.findByEmail(email).orElse(null);
        if (user == null) {
            // Burn a BCrypt match so unknown accounts take as long as real ones (anti-enumeration).
            passwordEncoder.matches(request.password(), dummyPasswordHash);
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Invalid email or password");
        }
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Invalid email or password");
        }
        return issueTokens(user);
    }

    /**
     * Rotates the refresh token: the presented token is single-use, so it is deleted and a new
     * pair is issued for the same user.
     */
    @Transactional
    public AuthResponse refresh(RefreshRequest request) {
        final Long userId;
        try {
            userId = jwtService.parseRefreshTokenSubject(request.refreshToken());
        } catch (JwtException | IllegalArgumentException ex) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "Invalid or expired refresh token");
        }
        // Atomically claim the session: the SELECT ... FOR UPDATE serializes concurrent replays
        // of the same token, so only one request can rotate it (the other finds nothing).
        RefreshToken stored = refreshTokens.findByTokenHashForUpdate(hash(request.refreshToken()))
                .filter(token -> !token.isExpired() && token.getUser().getId().equals(userId))
                .orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED, "Invalid or expired refresh token"));
        refreshTokens.delete(stored);
        return issueTokens(stored.getUser());
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
