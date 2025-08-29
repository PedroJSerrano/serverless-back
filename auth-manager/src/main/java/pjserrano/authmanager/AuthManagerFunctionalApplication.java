package pjserrano.authmanager;

import org.springframework.cloud.function.context.FunctionalSpringApplication;

public class AuthManagerFunctionalApplication extends FunctionalSpringApplication {
    public static void main(String[] args) {
        FunctionalSpringApplication.run(AuthManagerFunctionalApplication.class, args);
    }
}
