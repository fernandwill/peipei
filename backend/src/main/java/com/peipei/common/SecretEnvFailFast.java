package com.peipei.common;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;
import java.util.List;

/**
 * Fails fast at startup when a non-dev profile is active but the secret environment variables
 * are not set, so a misconfigured environment can never silently run with the dev-only
 * placeholder secrets from application.yml (§36).
 *
 * <p>Local development (no active profile, or the {@code dev} profile) is exempt and uses the
 * documented dev defaults.
 *
 * <p>Registered programmatically from {@link com.peipei.PeipeiApplication} via
 * {@code EnvironmentPostProcessorApplicationListener.with(EnvironmentPostProcessorsFactory.of(...))}.
 */
public class SecretEnvFailFast implements EnvironmentPostProcessor {

    private static final List<String> REQUIRED_SECRETS = List.of(
            "JWT_SECRET",
            "JWT_REFRESH_SECRET",
            "API_KEY_HASH_PEPPER",
            "WEBHOOK_SIGNING_SECRET");

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        boolean nonDevProfileActive = Arrays.stream(environment.getActiveProfiles())
                .anyMatch(profile -> !"dev".equals(profile));
        if (!nonDevProfileActive) {
            return;
        }

        List<String> missing = REQUIRED_SECRETS.stream()
                .filter(name -> {
                    String value = environment.getProperty(name);
                    return value == null || value.isBlank();
                })
                .toList();

        if (!missing.isEmpty()) {
            throw new IllegalStateException(
                    "Non-dev profile is active but the following secret environment variables are missing: "
                            + missing
                            + ". Refusing to start with dev-only placeholder secrets. Set them via the "
                            + "environment (see .env.example and §36 of the spec).");
        }
    }
}
