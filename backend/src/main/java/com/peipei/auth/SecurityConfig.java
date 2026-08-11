package com.peipei.auth;

import com.peipei.common.ApiError;
import tools.jackson.databind.ObjectMapper;
import com.peipei.common.ErrorCode;
import com.peipei.common.RequestIdContext;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * Security configuration (dev steps 3-5, §26): JWT bearer auth for the dashboard and API,
 * fully stateless sessions, and 401/403 responses in the standard §28 error envelope. API-key
 * authentication for the merchant API arrives in dev step 5.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService, ObjectMapper objectMapper)
            throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jsonEntryPoint(objectMapper, ErrorCode.UNAUTHORIZED, "Authentication required"))
                        .accessDeniedHandler(jsonAccessDeniedHandler(objectMapper)))
                .authorizeHttpRequests(auth -> auth
                        // Auth endpoints are anonymous: the credentials travel in the body
                        // (password / refresh token), not in an Authorization header.
                        .requestMatchers("/api/auth/register", "/api/auth/login",
                                "/api/auth/refresh", "/api/auth/logout").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    private static AuthenticationEntryPoint jsonEntryPoint(ObjectMapper mapper, ErrorCode code, String message) {
        return (request, response, ex) -> writeError(mapper, response, HttpStatus.UNAUTHORIZED, code, message);
    }

    private static AccessDeniedHandler jsonAccessDeniedHandler(ObjectMapper mapper) {
        return (request, response, ex) ->
                writeError(mapper, response, HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN, "Insufficient permissions");
    }

    private static void writeError(ObjectMapper mapper, HttpServletResponse response, HttpStatus status,
                                   ErrorCode code, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), new ApiError(code.name(), message, RequestIdContext.get()));
    }
}
