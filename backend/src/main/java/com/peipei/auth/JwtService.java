package com.peipei.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Issues and validates HS256 JWTs (dev step 3, §38). Access tokens are short-lived and carry
 * the caller's identity as claims. Refresh tokens are long-lived, signed with a separate
 * secret, and only meaningful while a matching hashed row exists in {@code refresh_tokens}.
 */
@Service
public class JwtService {

    private static final String CLAIM_TYPE = "type";
    private static final String CLAIM_TYPE_ACCESS = "access";
    private static final String CLAIM_TYPE_REFRESH = "refresh";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLE = "role";

    private final SecretKey accessKey;
    private final SecretKey refreshKey;
    private final Duration accessTtl;
    private final Duration refreshTtl;

    public JwtService(@Value("${app.jwt.secret}") String accessSecret,
                      @Value("${app.jwt.refresh-secret}") String refreshSecret,
                      @Value("${app.jwt.access-ttl:15m}") Duration accessTtl,
                      @Value("${app.jwt.refresh-ttl:30d}") Duration refreshTtl) {
        this.accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
        this.accessTtl = accessTtl;
        this.refreshTtl = refreshTtl;
    }

    public Duration accessTtl() {
        return accessTtl;
    }

    public Duration refreshTtl() {
        return refreshTtl;
    }

    public String generateAccessToken(User user) {
        return buildToken(user, accessKey, accessTtl, CLAIM_TYPE_ACCESS);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, refreshKey, refreshTtl, CLAIM_TYPE_REFRESH);
    }

    /** Validates an access token and returns the authenticated principal. Throws {@link JwtException} when invalid. */
    public AuthenticatedUser parseAccessToken(String token) {
        Claims claims = parse(token, accessKey, CLAIM_TYPE_ACCESS);
        return new AuthenticatedUser(
                Long.valueOf(claims.getSubject()),
                claims.get(CLAIM_EMAIL, String.class),
                UserRole.valueOf(claims.get(CLAIM_ROLE, String.class)));
    }

    /** Validates a refresh token and returns the subject (user id). Throws {@link JwtException} when invalid. */
    public Long parseRefreshTokenSubject(String token) {
        return Long.valueOf(parse(token, refreshKey, CLAIM_TYPE_REFRESH).getSubject());
    }

    private Claims parse(String token, SecretKey key, String expectedType) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        if (!expectedType.equals(claims.get(CLAIM_TYPE, String.class))) {
            throw new JwtException("Unexpected token type");
        }
        return claims;
    }

    private String buildToken(User user, SecretKey key, Duration ttl, String type) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .claim(CLAIM_TYPE, type)
                .claim(CLAIM_EMAIL, user.getEmail())
                .claim(CLAIM_ROLE, user.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .signWith(key)
                .compact();
    }
}
