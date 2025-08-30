package pjserrano.usermanager.infrastructure.adapter.in.web;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import pjserrano.usermanager.application.service.DeleteUserService;
import pjserrano.usermanager.application.service.RegisterUserService;
import pjserrano.usermanager.application.service.UpdateUserService;

@Configuration
@AllArgsConstructor
public class UserApiFunctions {

    private final DeleteUserService deleteUserService;
    private final RegisterUserService registerUserService;
    private final UpdateUserService updateUserService;
}
