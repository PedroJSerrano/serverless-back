package dev.pedronube.userprofile.application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.pedronube.userprofile.domain.port.in.DeleteUserUseCase;
import dev.pedronube.userprofile.domain.port.out.DeleteUserRepositoryPort;

@Configuration
public class DeleteUserService {

    @Bean
    public DeleteUserUseCase deleteUser(DeleteUserRepositoryPort userRepositoryPort) {
        return null;
    }
}
