package dev.pedronube.authenticationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"dev.pedronube.authenticationservice", "dev.pedronube.cloudinfrastructurecommons"})
@Import(ContextFunctionCatalogAutoConfiguration.class)
public class AuthenticationServiceConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceConfiguration.class, args);
    }
}
