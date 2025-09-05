package dev.pedronube.usermanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"dev.pedronube.usermanagementservice", "dev.pedronube.cloudinfrastructurecommons"})
public class UserManagerConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(UserManagerConfiguration.class, args);
    }
}
