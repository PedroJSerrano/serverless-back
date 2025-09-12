package dev.pedronube.userprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"dev.pedronube.userprofile", "dev.pedronube.cloudinfrastructurecommons"})
public class UserProfileConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(UserProfileConfiguration.class, args);
    }
}
