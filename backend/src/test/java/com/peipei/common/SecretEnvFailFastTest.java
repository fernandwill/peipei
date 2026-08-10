package com.peipei.common;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SecretEnvFailFastTest {

    private final SecretEnvFailFast processor = new SecretEnvFailFast();

    @Test
    void failsWhenNonDevProfileAndSecretsMissing() {
        MockEnvironment env = new MockEnvironment();
        env.setActiveProfiles("prod");

        assertThrows(IllegalStateException.class, () -> processor.postProcessEnvironment(env, null));
    }

    @Test
    void failsWhenOnlySomeSecretsMissing() {
        MockEnvironment env = new MockEnvironment();
        env.setActiveProfiles("staging");
        env.setProperty("JWT_SECRET", "set");
        env.setProperty("JWT_REFRESH_SECRET", "set");
        env.setProperty("API_KEY_HASH_PEPPER", "set");
        // WEBHOOK_SIGNING_SECRET intentionally left unset

        assertThrows(IllegalStateException.class, () -> processor.postProcessEnvironment(env, null));
    }

    @Test
    void passesWhenNonDevProfileAndAllSecretsSet() {
        MockEnvironment env = new MockEnvironment();
        env.setActiveProfiles("prod");
        setAllSecrets(env);

        assertDoesNotThrow(() -> processor.postProcessEnvironment(env, null));
    }

    @Test
    void passesForDevProfileEvenWithoutSecrets() {
        MockEnvironment env = new MockEnvironment();
        env.setActiveProfiles("dev");

        assertDoesNotThrow(() -> processor.postProcessEnvironment(env, null));
    }

    @Test
    void passesWithoutAnyProfile() {
        assertDoesNotThrow(() -> processor.postProcessEnvironment(new MockEnvironment(), null));
    }

    private void setAllSecrets(MockEnvironment env) {
        env.setProperty("JWT_SECRET", "set");
        env.setProperty("JWT_REFRESH_SECRET", "set");
        env.setProperty("API_KEY_HASH_PEPPER", "set");
        env.setProperty("WEBHOOK_SIGNING_SECRET", "set");
    }
}
