package dev.pedronube.usermanagementservice.application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.pedronube.usermanagementservice.domain.port.in.DeleteUserUseCase;
import dev.pedronube.usermanagementservice.domain.port.out.DeleteUserRepositoryPort;

@Configuration
public class DeleteUserService {

    @Bean
    public DeleteUserUseCase deleteUser(DeleteUserRepositoryPort userRepositoryPort) {
        return null;
    }
}
