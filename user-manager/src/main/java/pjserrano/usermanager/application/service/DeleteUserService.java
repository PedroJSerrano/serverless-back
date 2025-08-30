package pjserrano.usermanager.application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pjserrano.usermanager.domain.port.in.DeleteUserUseCase;
import pjserrano.usermanager.domain.port.out.DeleteUserRepositoryPort;

@Configuration
public class DeleteUserService {

    @Bean
    public DeleteUserUseCase deleteUser(DeleteUserRepositoryPort userRepositoryPort) {
        return null;
    }
}
