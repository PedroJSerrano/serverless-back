package dev.pedronube.cognitointegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"dev.pedronube.cognitointegration", "dev.pedronube.cloudinfrastructurecommons"})
@Import(ContextFunctionCatalogAutoConfiguration.class)
public class CognitoIntegrationConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(CognitoIntegrationConfiguration.class, args);
    }
}
