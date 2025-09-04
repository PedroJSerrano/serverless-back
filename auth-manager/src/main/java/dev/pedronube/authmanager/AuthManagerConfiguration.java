package dev.pedronube.authmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"dev.pedronube.authmanager", "dev.pedronube.awscommon"})
@Import(ContextFunctionCatalogAutoConfiguration.class)
public class AuthManagerConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(AuthManagerConfiguration.class, args);
    }
}
