package pjserrano.authmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"pjserrano.authmanager", "pjserrano.awscommon"})
public class AuthManagerConfiguration {
    public static void main(String[] args) { SpringApplication.run(AuthManagerConfiguration.class, args); }
}
