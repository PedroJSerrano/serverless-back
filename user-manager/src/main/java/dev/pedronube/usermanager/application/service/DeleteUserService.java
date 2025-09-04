package dev.pedronube.usermanager.application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.pedronube.usermanager.domain.port.in.DeleteUserUseCase;
import dev.pedronube.usermanager.domain.port.out.DeleteUserRepositoryPort;

@Configuration
public class DeleteUserService {

    @Bean
    public DeleteUserUseCase deleteUser(DeleteUserRepositoryPort userRepositoryPort) {
        return null;
    }
}
