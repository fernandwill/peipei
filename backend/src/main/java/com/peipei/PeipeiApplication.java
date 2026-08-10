package com.peipei;

import com.peipei.common.SecretEnvFailFast;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.support.EnvironmentPostProcessorApplicationListener;
import org.springframework.boot.support.EnvironmentPostProcessorsFactory;

@SpringBootApplication
public class PeipeiApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PeipeiApplication.class);
        // Fail fast when a non-dev profile is active but secret env vars are missing (§36).
        application.addListeners(EnvironmentPostProcessorApplicationListener.with(
                EnvironmentPostProcessorsFactory.of(SecretEnvFailFast.class)));
        application.run(args);
    }
}
