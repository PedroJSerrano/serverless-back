package pjserrano.usermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"pjserrano.usermanager", "pjserrano.awscommon"})
public class UserManagerConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(UserManagerConfiguration.class, args);
    }
}
