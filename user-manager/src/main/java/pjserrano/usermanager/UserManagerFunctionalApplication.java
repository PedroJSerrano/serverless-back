package pjserrano.usermanager;

import org.springframework.cloud.function.context.FunctionalSpringApplication;

public class UserManagerFunctionalApplication extends FunctionalSpringApplication {
    public static void main(String[] args) {
        FunctionalSpringApplication.run(UserManagerFunctionalApplication.class, args);
    }
}
