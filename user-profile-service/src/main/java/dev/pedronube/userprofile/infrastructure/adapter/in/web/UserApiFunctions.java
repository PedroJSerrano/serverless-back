package dev.pedronube.userprofile.infrastructure.adapter.in.web;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import dev.pedronube.userprofile.application.service.DeleteUserService;
import dev.pedronube.userprofile.application.service.RegisterUserService;
import dev.pedronube.userprofile.application.service.UpdateUserService;

@Configuration
@AllArgsConstructor
public class UserApiFunctions {

    private final DeleteUserService deleteUserService;
    private final RegisterUserService registerUserService;
    private final UpdateUserService updateUserService;
}
